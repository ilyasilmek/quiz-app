-- Harden match data visibility. Clients cannot insert/update match state directly.

alter table public.matches enable row level security;
alter table public.match_answers enable row level security;

revoke insert, update, delete on public.matches from anon, authenticated;
revoke insert, update, delete on public.match_answers from anon, authenticated;

 drop policy if exists "players can read own matches" on public.matches;
create policy "players can read own matches"
  on public.matches for select to authenticated
  using (auth.uid() = player_a or auth.uid() = player_b);

drop policy if exists "players can read own answers" on public.match_answers;
create policy "players can read own answers"
  on public.match_answers for select to authenticated
  using (auth.uid() = player_id);

revoke execute on function public.advance_match_after_answer(uuid) from anon, authenticated;
