import { createClient } from 'npm:@supabase/supabase-js@2';

const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type',
};
const json = (body: unknown, status = 200) => new Response(JSON.stringify(body), { status, headers: { ...corsHeaders, 'Content-Type': 'application/json' } });

Deno.serve(async (req) => {
  if (req.method === 'OPTIONS') return new Response('ok', { headers: corsHeaders });
  if (req.method !== 'POST') return json({ error: 'method_not_allowed' }, 405);
  const auth = req.headers.get('Authorization');
  if (!auth?.startsWith('Bearer ')) return json({ error: 'unauthorized' }, 401);
  const url = Deno.env.get('SUPABASE_URL');
  const serviceKey = Deno.env.get('SUPABASE_SERVICE_ROLE_KEY');
  if (!url || !serviceKey) return json({ error: 'server_not_configured' }, 500);

  const userClient = createClient(url, Deno.env.get('SUPABASE_ANON_KEY') ?? serviceKey, { global: { headers: { Authorization: auth } } });
  const adminClient = createClient(url, serviceKey);
  const { data: { user }, error } = await userClient.auth.getUser();
  if (error || !user) return json({ error: 'unauthorized' }, 401);

  const payload = await req.json().catch(() => null);
  const rounds = Math.min(Math.max(Number(payload?.total_rounds ?? 10), 1), 50);
  if (!Number.isInteger(rounds)) return json({ error: 'validation_failed' }, 422);

  const { data: match, error: matchError } = await adminClient.from('matches').insert({ player_a: user.id, total_rounds: rounds, status: 'active', started_at: new Date().toISOString() }).select('id,status,total_rounds,current_round').single();
  if (matchError || !match) return json({ error: 'match_create_failed' }, 500);

  const { data: questions, error: questionError } = await adminClient
    .from('questions')
    .select('id')
    .eq('is_published', true)
    .limit(rounds * 5);
  if (questionError || !questions?.length) return json({ error: 'question_pool_unavailable' }, 503);

  const shuffled = questions.sort(() => Math.random() - 0.5).slice(0, rounds);
  const now = Date.now();
  const rows = shuffled.map((q, index) => ({
    match_id: match.id,
    round_no: index + 1,
    question_id: q.id,
    opens_at: new Date(now + index * 20000).toISOString(),
    closes_at: new Date(now + index * 20000 + 15000).toISOString(),
  }));
  const { error: roundError } = await adminClient.from('match_rounds').insert(rows);
  if (roundError) return json({ error: 'round_create_failed' }, 500);

  return json({ match_id: match.id, status: match.status, total_rounds: rounds }, 201);
});
