-- PostgreSQL/Supabase başlangıç şeması.

create type submission_status as enum (
  'pending_validation','pending_review','approved','rejected','published','archived'
);

create table if not exists question_submissions (
  id uuid primary key,
  author_id uuid not null,
  category_id uuid not null,
  question_text text not null,
  options jsonb not null,
  correct_index smallint not null,
  explanation text,
  source_url text,
  normalized_hash text not null,
  status submission_status not null default 'pending_validation',
  rejection_reason text,
  reviewed_by uuid,
  reviewed_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_question_submissions_status on question_submissions(status);
create index if not exists idx_question_submissions_hash on question_submissions(normalized_hash);

create table if not exists question_duplicate_candidates (
  submission_id uuid not null references question_submissions(id),
  existing_question_id uuid not null,
  similarity numeric(5,4) not null,
  method text not null,
  primary key (submission_id, existing_question_id)
);

create table if not exists admin_audit_log (
  id uuid primary key,
  admin_id uuid not null,
  action text not null,
  entity_type text not null,
  entity_id uuid not null,
  metadata jsonb,
  created_at timestamptz not null default now()
);
