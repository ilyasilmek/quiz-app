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
  const db = createClient(url, serviceKey);
  const { data: { user }, error: authError } = await userClient.auth.getUser();
  if (authError || !user) return json({ error: 'unauthorized' }, 401);

  const payload = await req.json().catch(() => null);
  const matchId = String(payload?.match_id ?? '');
  if (!matchId) return json({ error: 'validation_failed' }, 422);

  const { data: match } = await db.from('matches').select('id,player_a,player_b,status,total_rounds,current_round,started_at,finished_at,winner_id').eq('id', matchId).single();
  if (!match || (match.player_a !== user.id && match.player_b !== user.id)) return json({ error: 'forbidden' }, 403);

  const { data: rounds, error: roundsError } = await db
    .from('match_rounds')
    .select('round_no,question_id,opens_at,closes_at')
    .eq('match_id', matchId)
    .order('round_no');
  if (roundsError) return json({ error: 'rounds_unavailable' }, 500);

  const questionIds = (rounds ?? []).map((r) => r.question_id);
  const { data: questions, error: questionError } = questionIds.length
    ? await db.from('published_questions').select('id,question_text,options,category_id,explanation').in('id', questionIds)
    : { data: [], error: null };
  if (questionError) return json({ error: 'questions_unavailable' }, 500);

  const byId = new Map((questions ?? []).map((q) => [q.id, q]));
  return json({
    match,
    rounds: (rounds ?? []).map((round) => ({
      round_no: round.round_no,
      opens_at: round.opens_at,
      closes_at: round.closes_at,
      question: byId.get(round.question_id) ?? null,
    })),
  });
});
