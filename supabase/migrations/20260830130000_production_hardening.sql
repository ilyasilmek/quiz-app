-- Production hardening for moderated questions and server-authoritative answers.

create table if not exists public.submission_duplicate_candidates (
  submission_id uuid not null references public.question_submissions(id) on delete cascade,
  duplicate_submission_id uuid not null references public.question_submissions(id) on delete cascade,
  similarity numeric(5,4) not null check (similarity >= 0 and similarity <= 1),
  method text not null,
  created_at timestamptz not null default now(),
  primary key (submission_id, duplicate_submission_id)
);

create index if not exists idx_submission_duplicates_submission
  on public.submission_duplicate_candidates(submission_id);

create index if not exists idx_submission_duplicates_match
  on public.submission_duplicate_candidates(duplicate_submission_id);

alter table public.submission_duplicate_candidates enable row level security;

-- Only the private service role / admin functions should create duplicate candidates.
-- There is intentionally no anonymous/authenticated INSERT policy.

create unique index if not exists uq_question_submissions_author_request
  on public.question_submissions(author_id, client_request_id);

create unique index if not exists uq_questions_normalized_hash
  on public.questions(normalized_hash)
  where is_published = true;

create or replace function public.submit_match_answer(
  p_match_id uuid,
  p_question_id uuid,
  p_selected_index smallint,
  p_elapsed_ms bigint
)
returns table(is_correct boolean, awarded_points integer, match_status match_status)
language plpgsql
security definer
set search_path = public
as $$
declare
  v_match matches%rowtype;
  v_question questions%rowtype;
  v_correct boolean;
  v_points integer;
  v_player uuid := auth.uid();
  v_index smallint;
begin
  if v_player is null then
    raise exception 'unauthorized';
  end if;

  if p_elapsed_ms < 0 or p_elapsed_ms > 60000 then
    raise exception 'invalid_elapsed_ms';
  end if;

  select * into v_match
  from matches
  where id = p_match_id
    and (player_a = v_player or player_b = v_player)
  for update;

  if v_match.id is null then raise exception 'match_not_found'; end if;
  if v_match.status <> 'active' then raise exception 'match_not_active'; end if;

  v_index := v_match.current_index;
  if v_index < 0 or v_index >= cardinality(v_match.question_ids) then
    raise exception 'invalid_question_index';
  end if;
  if v_match.question_ids[v_index + 1] <> p_question_id then
    raise exception 'question_out_of_order';
  end if;

  select * into v_question from questions where id = p_question_id and is_published = true;
  if v_question.id is null then raise exception 'question_not_found'; end if;

  if p_selected_index is null or p_selected_index < 0 or p_selected_index > 3 then
    v_correct := false;
  else
    v_correct := p_selected_index = v_question.correct_index;
  end if;

  v_points := public.calculate_answer_points(p_elapsed_ms, v_correct);

  insert into match_answers(match_id, player_id, question_id, selected_index, elapsed_ms, awarded_points, is_correct)
  values(p_match_id, v_player, p_question_id, p_selected_index, p_elapsed_ms, v_points, v_correct)
  on conflict (match_id, player_id, question_id) do nothing;

  if v_player = v_match.player_a then
    update matches set player_a_score = player_a_score + v_points where id = p_match_id;
  else
    update matches set player_b_score = player_b_score + v_points where id = p_match_id;
  end if;

  perform public.advance_match_after_answer(p_match_id);

  return query
    select v_correct, v_points, m.status
    from matches m where m.id = p_match_id;
end;
$$;

revoke all on function public.submit_match_answer(uuid, uuid, smallint, bigint) from public;
grant execute on function public.submit_match_answer(uuid, uuid, smallint, bigint) to authenticated;
