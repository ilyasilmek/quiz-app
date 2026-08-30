-- Bilgi Arenası content platform foundation.
-- Apply with Supabase migrations in a disposable environment first.

create extension if not exists pgcrypto;

create type public.submission_status as enum (
  'pending_validation',
  'pending_review',
  'approved',
  'rejected',
  'published',
  'archived'
);

create table if not exists public.categories (
  id uuid primary key default gen_random_uuid(),
  slug text not null unique,
  name text not null unique,
  is_active boolean not null default true,
  created_at timestamptz not null default now()
);

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  display_name text not null default 'Oyuncu',
  level integer not null default 1 check (level > 0),
  xp integer not null default 0 check (xp >= 0),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.admin_roles (
  user_id uuid primary key references auth.users(id) on delete cascade,
  role text not null check (role in ('admin', 'moderator', 'content_editor')),
  is_active boolean not null default true,
  created_at timestamptz not null default now()
);

create table if not exists public.questions (
  id uuid primary key default gen_random_uuid(),
  category_id uuid not null references public.categories(id),
  question_text text not null,
  options jsonb not null check (jsonb_typeof(options) = 'array'),
  correct_index smallint not null check (correct_index between 0 and 3),
  explanation text,
  source_url text,
  author_id uuid references auth.users(id) on delete set null,
  normalized_hash text not null,
  fingerprint text not null,
  is_published boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create unique index if not exists uq_questions_normalized_hash on public.questions(normalized_hash);
create index if not exists idx_questions_category_published on public.questions(category_id, is_published);

create table if not exists public.question_submissions (
  id uuid primary key default gen_random_uuid(),
  author_id uuid not null references auth.users(id) on delete cascade,
  category_id uuid not null references public.categories(id),
  question_text text not null check (length(trim(question_text)) >= 10),
  options jsonb not null check (jsonb_typeof(options) = 'array'),
  correct_index smallint not null check (correct_index between 0 and 3),
  explanation text,
  source_url text,
  normalized_hash text not null,
  fingerprint text not null,
  status public.submission_status not null default 'pending_validation',
  rejection_reason text,
  reviewed_by uuid references auth.users(id) on delete set null,
  reviewed_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_submissions_author on public.question_submissions(author_id, created_at desc);
create index if not exists idx_submissions_status on public.question_submissions(status, created_at asc);
create index if not exists idx_submissions_hash on public.question_submissions(normalized_hash);

create table if not exists public.question_duplicate_candidates (
  submission_id uuid not null references public.question_submissions(id) on delete cascade,
  existing_question_id uuid not null references public.questions(id) on delete cascade,
  similarity numeric(5,4) not null check (similarity between 0 and 1),
  method text not null check (method in ('exact_hash', 'fingerprint', 'fuzzy', 'embedding')),
  created_at timestamptz not null default now(),
  primary key (submission_id, existing_question_id)
);

create table if not exists public.admin_audit_log (
  id uuid primary key default gen_random_uuid(),
  admin_id uuid not null references auth.users(id) on delete restrict,
  action text not null,
  entity_type text not null,
  entity_id uuid,
  metadata jsonb,
  created_at timestamptz not null default now()
);

-- Published read model: public clients may read only approved/published content.
create or replace view public.published_questions as
select
  q.id,
  q.category_id,
  q.question_text,
  q.options,
  q.explanation
from public.questions q
where q.is_published = true;

-- Profiles are created automatically after Auth signup.
create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer set search_path = public
as $$
begin
  insert into public.profiles (id, display_name)
  values (new.id, coalesce(new.raw_user_meta_data->>'display_name', 'Oyuncu'))
  on conflict (id) do nothing;
  return new;
end;
$$;

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
after insert on auth.users
for each row execute procedure public.handle_new_user();

-- RLS: every exposed table is locked down explicitly.
alter table public.categories enable row level security;
alter table public.profiles enable row level security;
alter table public.admin_roles enable row level security;
alter table public.questions enable row level security;
alter table public.question_submissions enable row level security;
alter table public.question_duplicate_candidates enable row level security;
alter table public.admin_audit_log enable row level security;

revoke all on table public.admin_roles from anon, authenticated;
revoke all on table public.question_duplicate_candidates from anon, authenticated;
revoke all on table public.admin_audit_log from anon, authenticated;
revoke all on table public.questions from anon, authenticated;
revoke all on table public.question_submissions from anon;

-- Public categories.
create policy categories_public_read on public.categories
for select to anon, authenticated
using (is_active = true);

-- Own profile only.
create policy profiles_self_read on public.profiles
for select to authenticated
using ((select auth.uid()) = id);
create policy profiles_self_update on public.profiles
for update to authenticated
using ((select auth.uid()) = id)
with check ((select auth.uid()) = id);

-- Player may submit; only their own pending submission is visible to them.
grant select, insert on public.categories to anon, authenticated;
grant select, insert on public.question_submissions to authenticated;
create policy submissions_self_read on public.question_submissions
for select to authenticated
using ((select auth.uid()) = author_id);
create policy submissions_self_insert on public.question_submissions
for insert to authenticated
with check ((select auth.uid()) = author_id and status = 'pending_validation');

-- Only published question data is exposed through the view.
grant select on public.published_questions to anon, authenticated;

-- Admin authorization helper. The table itself remains inaccessible to normal clients.
create or replace function public.is_admin()
returns boolean
language sql
stable
security definer
set search_path = public
as $$
  select exists (
    select 1 from public.admin_roles r
    where r.user_id = (select auth.uid())
      and r.is_active = true
      and r.role in ('admin', 'moderator', 'content_editor')
  );
$$;

-- Admin audit is written server-side only.
comment on table public.admin_audit_log is 'Never exposed to player API. Write through private admin service only.';
