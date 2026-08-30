-- Idempotent question submission support.
alter table public.question_submissions
  add column if not exists client_request_id uuid;

create unique index if not exists uq_submission_author_request
  on public.question_submissions(author_id, client_request_id)
  where client_request_id is not null;
