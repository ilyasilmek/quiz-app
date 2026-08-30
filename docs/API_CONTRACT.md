# API Sözleşmesi v0.2

## Public player API

`POST /functions/v1/submit-question`
- Auth: Bearer user access token
- Body: `client_request_id`, `category_id`, `question`, `options[4]`, `correct_index`, optional `explanation`, optional `source_url`
- Response: submission id, status, duplicate candidate count, idempotent replay flag

`GET /v1/questions`
- Only published question data.
- Correct answer key is excluded until the server-authorized game session requires it.

`GET /v1/categories`
`POST /v1/matches`
`POST /v1/matches/{id}/answers`
`GET /v1/matches/{id}`
`GET /v1/leaderboard`
`GET /v1/me`

## Private admin API / Edge Functions

`GET /functions/v1/admin-question-queue?status=pending_review&limit=50`
`POST /functions/v1/admin-question-review`
`GET /admin/v1/question-submissions/{id}/duplicates`
`GET /admin/v1/audit-log`

Admin review actions are `approve`, `reject`, `publish`, `archive`.

## State machine

`pending_validation -> pending_review -> approved -> published`

Alternative terminal states: `rejected`, `archived`.

Publishing requires prior approval and a duplicate-free review result.

## Boundary

The player client contains no admin endpoint references and no service-role credential. The production deployment must place the admin API behind a separate private origin/network boundary.
