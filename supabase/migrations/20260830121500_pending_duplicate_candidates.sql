-- Detect duplicates against other submitted questions before publication.
create table if not exists public.submission_duplicate_candidates (
  submission_id uuid not null references public.question_submissions(id) on delete cascade,
  duplicate_submission_id uuid not null references public.question_submissions(id) on delete cascade,
  similarity numeric(5,4) not null check (similarity between 0 and 1),
  method text not null check (method in ('exact_hash', 'fingerprint', 'fuzzy')),
  created_at timestamptz not null default now(),
  primary key (submission_id, duplicate_submission_id),
  check (submission_id <> duplicate_submission_id)
);

create index if not exists idx_submission_duplicates_submission
  on public.submission_duplicate_candidates(submission_id, similarity desc);
create index if not exists idx_submission_duplicates_duplicate
  on public.submission_duplicate_candidates(duplicate_submission_id, similarity desc);

alter table public.submission_duplicate_candidates enable row level security;
revoke all on table public.submission_duplicate_candidates from anon, authenticated;
comment on table public.submission_duplicate_candidates is 'Private moderation data; written/read only by backend admin service.';
