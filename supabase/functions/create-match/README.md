# create-match

Creates a server-owned match and returns only the match metadata/question ids. The player app never receives correct answers from this endpoint.

Production deployment requirements:
- deploy as a Supabase Edge Function
- set `SUPABASE_URL`, `SUPABASE_ANON_KEY`, and `SUPABASE_SERVICE_ROLE_KEY`
- expose only the public function endpoint
- never ship the service-role key in either mobile app
