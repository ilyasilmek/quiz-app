-- Only the public Edge Function may create question submissions.
-- This closes the direct-table insertion path and preserves the player/admin boundary.
revoke insert on table public.question_submissions from authenticated;
revoke update, delete on table public.question_submissions from authenticated;

comment on table public.question_submissions is
  'Player submissions are written by the public submit-question Edge Function only; direct client writes are revoked.';
