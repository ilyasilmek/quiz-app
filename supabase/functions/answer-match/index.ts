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
  const roundNo = Number(payload?.round_no);
  const optionIndex = Number(payload?.option_index);
  const clientAnsweredAt = payload?.answered_at ? new Date(String(payload.answered_at)) : null;
  if (!matchId || !Number.isInteger(roundNo) || roundNo < 1 || !Number.isInteger(optionIndex) || optionIndex < 0 || optionIndex > 3) {
    return json({ error: 'validation_failed' }, 422);
  }

  const { data: match } = await db.from('matches').select('id,player_a,player_b,status,total_rounds').eq('id', matchId).single();
  if (!match || (match.player_a !== user.id && match.player_b !== user.id)) return json({ error: 'forbidden' }, 403);
  if (match.status !== 'active') return json({ error: 'match_not_active' }, 409);

  const { data: round } = await db.from('match_rounds').select('id,question_id,opens_at,closes_at').eq('match_id', matchId).eq('round_no', roundNo).single();
  if (!round) return json({ error: 'round_not_found' }, 404);

  const now = new Date();
  if (now < new Date(round.opens_at) || now > new Date(round.closes_at)) return json({ error: 'round_closed' }, 409);

  const { data: existing } = await db.from('match_answers').select('id').eq('match_round_id', round.id).eq('player_id', user.id).maybeSingle();
  if (existing) return json({ error: 'already_answered' }, 409);

  const { data: question } = await db.from('questions').select('correct_index').eq('id', round.question_id).eq('is_published', true).single();
  if (!question) return json({ error: 'question_not_found' }, 404);

  const elapsedMs = Math.max(0, now.getTime() - new Date(round.opens_at).getTime());
  const isCorrect = optionIndex === question.correct_index;
  const score = isCorrect ? Math.max(100, 100 + Math.max(0, 15000 - elapsedMs) * 12 / 1000) : 0;
  const serverScore = Math.floor(score);

  const { error: insertError } = await db.from('match_answers').insert({
    match_round_id: round.id,
    player_id: user.id,
    option_index: optionIndex,
    answered_at: now.toISOString(),
    answer_time_ms: Math.min(elapsedMs, 15000),
    is_correct: isCorrect,
    score_awarded: serverScore,
  });
  if (insertError) return json({ error: insertError.code === '23505' ? 'already_answered' : 'answer_save_failed' }, insertError.code === '23505' ? 409 : 500);

  return json({
    ok: true,
    correct: isCorrect,
    score_awarded: serverScore,
    answer_time_ms: Math.min(elapsedMs, 15000),
    server_time: now.toISOString(),
    // Never return correct_index to the player before round resolution.
  });
});
