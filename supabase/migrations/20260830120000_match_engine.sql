create type if not exists match_status as enum ('waiting','active','completed','abandoned','expired');

create table if not exists matches (
  id uuid primary key default gen_random_uuid(),
  mode text not null check (mode in ('solo','duel')),
  status match_status not null default 'active',
  player_a uuid not null,
  player_b uuid,
  question_ids uuid[] not null,
  current_index smallint not null default 0,
  player_a_score integer not null default 0,
  player_b_score integer not null default 0,
  started_at timestamptz not null default now(),
  completed_at timestamptz,
  expires_at timestamptz not null default (now() + interval '30 minutes')
);

create table if not exists match_answers (
  match_id uuid not null references matches(id) on delete cascade,
  player_id uuid not null,
  question_id uuid not null,
  selected_index smallint,
  answered_at timestamptz not null default now(),
  elapsed_ms bigint not null check (elapsed_ms >= 0 and elapsed_ms <= 60000),
  awarded_points integer not null default 0,
  is_correct boolean not null default false,
  primary key (match_id, player_id, question_id)
);

create index if not exists idx_matches_player_a on matches(player_a);
create index if not exists idx_matches_player_b on matches(player_b);
create index if not exists idx_matches_status on matches(status);
create index if not exists idx_match_answers_match on match_answers(match_id);

alter table matches enable row level security;
alter table match_answers enable row level security;

create policy "players can read own matches" on matches
for select using (auth.uid() = player_a or auth.uid() = player_b);

create policy "players can read own answers" on match_answers
for select using (auth.uid() = player_id);

create or replace function public.calculate_answer_points(p_elapsed_ms bigint, p_correct boolean)
returns integer
language sql
immutable
as $$
  select case when p_correct = false then 0
    else 100 + greatest(0, 50 - least(50, floor(p_elapsed_ms / 300)::integer)) end;
$$;

create or replace function public.advance_match_after_answer(p_match_id uuid)
returns void
language plpgsql
security definer
set search_path = public
as $$
declare
  v_match matches%rowtype;
  v_total_answers integer;
begin
  select * into v_match from matches where id = p_match_id for update;
  if v_match.id is null or v_match.status <> 'active' then return; end if;

  select count(distinct question_id) into v_total_answers
  from match_answers where match_id = p_match_id;

  if v_total_answers >= cardinality(v_match.question_ids) then
    update matches set status='completed', completed_at=now() where id=p_match_id;
  else
    update matches set current_index = least(current_index + 1, cardinality(question_ids)-1) where id=p_match_id;
  end if;
end;
$$;
