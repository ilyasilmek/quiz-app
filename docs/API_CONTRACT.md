# İlk API Sözleşmesi

## Public
POST /v1/question-submissions
GET /v1/questions
GET /v1/categories
POST /v1/matches
POST /v1/matches/{id}/answers
GET /v1/matches/{id}
GET /v1/leaderboard
GET /v1/me

## Admin
GET /admin/v1/question-submissions?status=pending_review
GET /admin/v1/question-submissions/{id}
POST /admin/v1/question-submissions/{id}/approve
POST /admin/v1/question-submissions/{id}/reject
POST /admin/v1/question-submissions/{id}/publish
POST /admin/v1/question-submissions/{id}/archive
GET /admin/v1/question-submissions/{id}/duplicates
GET /admin/v1/audit-log

Public API admin endpointlerini proxy etmez.
