-- Hardening pass: player clients must not bypass moderation through direct table writes.
revoke insert on public.categories from anon, authenticated;
revoke insert on public.question_submissions from authenticated;
revoke update, delete on public.question_submissions from authenticated;
revoke select on public.admin_roles from authenticated;
revoke select on public.questions from anon, authenticated;

-- The published read model is the only direct question read surface.
drop view if exists public.published_questions;
create view public.published_questions
with (security_invoker = true)
as
select
  q.id,
  q.category_id,
  q.question_text,
  q.options,
  q.explanation
from public.questions q
where q.is_published = true;

grant select on public.published_questions to anon, authenticated;
comment on view public.published_questions is 'Public read model. Never includes correct_index or moderation metadata.';
