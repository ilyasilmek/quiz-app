-- Server-authoritative match answer transaction.
-- The client submits intent; the database determines correctness and points.

create or replace function public.submit_match_answer(
  p_match_id uuid,
  p_question_id uuid,
  p_selected_index smallint,
  p_elapsed_ms bigint
)
returns table (
  is_correct boolean,
  awarded_points integer,
  match_status match_status,
  current_index smallint
)
language plpgsql
security definer
set search_path = public
as $$
declare
  v_match matches%rowtype;
  v_correct_index smallint;
  v_correct boolean;
  v_points integer;
  v_exists boolean;
  v_elapsed bigint;
begin
  if auth.uid() is null then raise exception 'unauthorized'; end if;
  if p_elapsed_ms < 0 or p_elapsed_ms > 15000 then raise exception 'invalid_elapsed_ms'; end if;
  if p_selected_index is null or p_selected_index < 0 or p_selected_index > 3 then raise exception 'invalid_option'; end if;

  select * into v_match from public.matches where id = p_match_id for update;
  if not found then raise exception 'match_not_found'; end if;
  if v_match.status <> 'active' then raise exception 'match_not_active'; end if;
  if auth.uid() <> v_match.player_a and auth.uid() <> v_match.player_b then raise exception 'forbidden'; end if;
  if v_match.question_ids[v_match.current_index + 1] <> p_question_id then raise exception 'question_not_current'; end if;

  select exists(
    select 1 from public.match_answers
    where match_id = p_match_id and player_id = auth.uid() and question_id = p_question_id
  ) into v_exists;
  if v_exists then raise exception 'answer_already_submitted'; end if;

  select q.correct_index into v_correct_index
  from public.questions q
  where q.id = p_question_id and q.is_published = true;
  if v_correct_index is null then raise exception 'question_not_found'; end if;

  v_correct := (p_selected_index = v_correct_index);
  v_elapsed := least(p_elapsed_ms, 15000);
  v_points := public.calculate_answer_points(v_elapsed, v_correct);

  insert into public.match_answers(
    match_id, player_id, question_id, selected_index, elapsed_ms, awarded_points, is_correct
  ) values (
    p_match_id, auth.uid(), p_question_id, p_selected_index, v_elapsed, v_points, v_correct
  );

  if auth.uid() = v_match.player_a then
    update public.matches
    set player_a_score = player_a_score + v_points
    where id = p_match_id;
  else
    update public.matches
    set player_b_score = player_b_score + v_points
    where id = p_match_id;
  end if;

  update public.matches m
  set current_index = current_index + 1,
      status = case when current_index + 1 >= cardinality(question_ids) then 'completed' else status end,
      completed_at = case when current_index + 1 >= cardinality(question_ids) then now() else completed_at end
  where id = p_match_id;

  select is_correct, awarded_points, m.status, m.current_index
  into is_correct, awarded_points, match_status, current_index
  from public.match_answers a
  join public.matches m on m.id = a.match_id
  where a.match_id = p_match_id and a.player_id = auth.uid() and a.question_id = p_question_id;

  return next;
end;
$$;

revoke all on function public.submit_match_answer(uuid, uuid, smallint, bigint) from public;
grant execute on function public.submit_match_answer(uuid, uuid, smallint, bigint) to authenticated;
