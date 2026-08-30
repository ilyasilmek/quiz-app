# Backend Foundation

## Public path
Player App calls only the public `submit-question` function and future public game endpoints. No admin function name, URL or credential is bundled into the player application.

## Private path
The future Admin App calls `admin-question-review` through a private deployment boundary. The function additionally checks authenticated identity against `admin_roles` before allowing review actions.

## Deployment rules
1. Store service credentials only as Supabase server-side secrets.
2. Do not place service-role credentials in Android/iOS builds.
3. Apply migrations in staging before production.
4. Enable and test RLS on all exposed tables.
5. Keep admin service on a distinct origin/network boundary at production launch; the current function split is the code-level boundary and is not by itself a network isolation guarantee.

## Submission lifecycle
Player -> submit-question -> validation/duplicate detection -> pending_review -> admin review -> published_questions.

The player receives status only; the moderation workspace and audit trail remain private.
