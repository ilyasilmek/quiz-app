create or replace function public.start_match(p_player_a uuid, p_mode text, p_question_ids uuid[])
returns matches
language plpgsql
security definer
set search_path = public
as $$
declare
  v_match matches%rowtype;
begin
  if p_mode not in ('solo','duel') then raise exception 'invalid_mode'; end if;
  if p_player_a is null or cardinality(p_question_ids) < 5 or cardinality(p_question_ids) > 20 then raise exception 'invalid_match'; end if;
  insert into matches(mode, player_a, question_ids)
  values (p_mode, p_player_a, p_question_ids)
  returning * into v_match;
  return v_match;
end;
$$;

create or replace function public.record_match_answer(
  p_match_id uuid,
  p_player_id uuid,
  p_selected_index smallint,
  p_elapsed_ms bigint
)
returns jsonb
language plpgsql
security definer
set search_path = public
as $$
declare
  v_match matches%rowtype;
  v_question questions%rowtype;
  v_question_id uuid;
  v_correct boolean;
  v_points integer;
  v_score integer;
begin
  select * into v_match from matches where id=p_match_id for update;
  if v_match.id is null or v_match.status <> 'active' then raise exception 'match_unavailable'; end if;
  if v_match.player_a <> p_player_id and coalesce(v_match.player_b,'00000000-0000-0000-0000-000000000000') <> p_player_id then raise exception 'forbidden'; end if;
  if v_match.expires_at <= now() then
    update matches set status='expired' where id=p_match_id;
    raise exception 'match_expired';
  end if;
  if p_selected_index < 0 or p_selected_index > 3 then raise exception 'invalid_answer'; end if;

  v_question_id := v_match.question_ids[v_match.current_index + 1];
  select * into v_question from questions where id=v_question_id and status='published';
  if v_question.id is null then raise exception 'question_unavailable'; end if;

  if exists (select 1 from match_answers where match_id=p_match_id and player_id=p_player_id and question_id=v_question_id) then
    raise exception 'answer_already_submitted';
  end if;

  v_correct := p_selected_index = v_question.correct_index;
  v_points := case when v_correct then 100 + greatest(0, 50 - least(50, floor(least(greatest(p_elapsed_ms,0),15000) / 300)::integer)) else 0 end;

  insert into match_answers(match_id, player_id, question_id, selected_index, elapsed_ms, awarded_points, is_correct)
  values (p_match_id, p_player_id, v_question_id, p_selected_index, least(greatest(p_elapsed_ms,0),15000), v_points, v_correct);

  if p_player_id = v_match.player_a then
    update matches set player_a_score = player_a_score + v_points where id=p_match_id returning player_a_score into v_score;
  else
    update matches set player_b_score = player_b_score + v_points where id=p_match_id returning player_b_score into v_score;
  end if;

  perform public.advance_match_after_answer(p_match_id);

  return jsonb_build_object(
    'question_id', v_question_id,
    'correct', v_correct,
    'awarded_points', v_points,
    'score', v_score
  );
end;
$$;
