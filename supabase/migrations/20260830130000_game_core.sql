-- Server-authoritative game core.
create type public.match_status as enum ('waiting','active','finished','cancelled','expired');

create table if not exists public.matches (
  id uuid primary key default gen_random_uuid(),
  player_a uuid not null references auth.users(id) on delete restrict,
  player_b uuid references auth.users(id) on delete restrict,
  status public.match_status not null default 'waiting',
  total_rounds smallint not null default 10 check (total_rounds between 1 and 50),
  current_round smallint not null default 0 check (current_round >= 0),
  started_at timestamptz,
  finished_at timestamptz,
  winner_id uuid references auth.users(id) on delete set null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  check (player_b is null or player_b <> player_a)
);

create table if not exists public.match_rounds (
  id uuid primary key default gen_random_uuid(),
  match_id uuid not null references public.matches(id) on delete cascade,
  round_no smallint not null check (round_no between 1 and 50),
  question_id uuid not null references public.questions(id) on delete restrict,
  opens_at timestamptz not null,
  closes_at timestamptz not null,
  created_at timestamptz not null default now(),
  unique(match_id, round_no)
);

create table if not exists public.match_answers (
  id uuid primary key default gen_random_uuid(),
  match_round_id uuid not null references public.match_rounds(id) on delete cascade,
  player_id uuid not null references auth.users(id) on delete restrict,
  option_index smallint not null check (option_index between 0 and 3),
  answered_at timestamptz not null default now(),
  answer_time_ms integer,
  is_correct boolean not null,
  score_awarded integer not null default 0 check (score_awarded >= 0),
  unique(match_round_id, player_id)
);

create index if not exists idx_matches_player_a on public.matches(player_a, created_at desc);
create index if not exists idx_matches_player_b on public.matches(player_b, created_at desc);
create index if not exists idx_rounds_match on public.match_rounds(match_id, round_no);
create index if not exists idx_answers_player on public.match_answers(player_id, answered_at desc);

alter table public.matches enable row level security;
alter table public.match_rounds enable row level security;
alter table public.match_answers enable row level security;

-- Players may only read matches they belong to. Writes go through server functions.
revoke all on public.matches from anon, authenticated;
revoke all on public.match_rounds from anon, authenticated;
revoke all on public.match_answers from anon, authenticated;
grant select on public.matches to authenticated;
grant select on public.match_rounds to authenticated;

drop policy if exists matches_participant_read on public.matches;
create policy matches_participant_read on public.matches
for select to authenticated
using ((select auth.uid()) = player_a or (select auth.uid()) = player_b);

drop policy if exists rounds_participant_read on public.match_rounds;
create policy rounds_participant_read on public.match_rounds
for select to authenticated
using (exists (
  select 1 from public.matches m
  where m.id = match_rounds.match_id
    and ((select auth.uid()) = m.player_a or (select auth.uid()) = m.player_b)
));

-- Correct answer and score remain server-side; match_answers are never directly writable by clients.
comment on table public.match_answers is 'Server authoritative writes only. Clients submit answer intents to Edge Function.';
