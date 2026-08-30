-- Production smoke checks. Run against a real Supabase database after migrations.
-- These are intentionally read-only assertions.

select assert_true(to_regclass('public.profiles') is not null, 'profiles exists');
select assert_true(to_regclass('public.categories') is not null, 'categories exists');
select assert_true(to_regclass('public.questions') is not null, 'questions exists');
select assert_true(to_regclass('public.question_submissions') is not null, 'question_submissions exists');
select assert_true(to_regclass('public.question_duplicate_candidates') is not null, 'question_duplicate_candidates exists');
select assert_true(to_regclass('public.submission_duplicate_candidates') is not null, 'submission_duplicate_candidates exists');
select assert_true(to_regclass('public.matches') is not null, 'matches exists');
select assert_true(to_regclass('public.match_answers') is not null, 'match_answers exists');
select assert_true(to_regclass('public.admin_roles') is not null, 'admin_roles exists');
select assert_true(to_regclass('public.admin_audit_log') is not null, 'admin_audit_log exists');
select assert_true(to_regprocedure('public.calculate_answer_points(bigint,boolean)') is not null, 'score function exists');
select assert_true(to_regprocedure('public.submit_match_answer(uuid,uuid,smallint,bigint)') is not null, 'answer RPC exists');
