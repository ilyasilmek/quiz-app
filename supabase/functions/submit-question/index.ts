import { createClient } from 'npm:@supabase/supabase-js@2';
import { normalizeTurkishText, questionFingerprintInput } from '../_shared/normalize.ts';

const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type',
};

const json = (body: unknown, status = 200) => new Response(JSON.stringify(body), {
  status,
  headers: { ...corsHeaders, 'Content-Type': 'application/json' },
});

const sha256 = async (value: string) => {
  const bytes = new TextEncoder().encode(value);
  const digest = await crypto.subtle.digest('SHA-256', bytes);
  return Array.from(new Uint8Array(digest)).map((b) => b.toString(16).padStart(2, '0')).join('');
};

Deno.serve(async (req) => {
  if (req.method === 'OPTIONS') return new Response('ok', { headers: corsHeaders });
  if (req.method !== 'POST') return json({ error: 'method_not_allowed' }, 405);

  const authHeader = req.headers.get('Authorization');
  if (!authHeader?.startsWith('Bearer ')) return json({ error: 'unauthorized' }, 401);

  const url = Deno.env.get('SUPABASE_URL');
  const serviceKey = Deno.env.get('SUPABASE_SERVICE_ROLE_KEY');
  if (!url || !serviceKey) return json({ error: 'server_not_configured' }, 500);

  const userClient = createClient(url, Deno.env.get('SUPABASE_ANON_KEY') ?? serviceKey, {
    global: { headers: { Authorization: authHeader } },
  });
  const adminClient = createClient(url, serviceKey);

  const { data: { user }, error: userError } = await userClient.auth.getUser();
  if (userError || !user) return json({ error: 'unauthorized' }, 401);

  const payload = await req.json().catch(() => null);
  if (!payload) return json({ error: 'invalid_json' }, 400);

  const requestIdRaw = String(payload.client_request_id ?? '').trim();
  const clientRequestId = requestIdRaw || crypto.randomUUID();
  const categoryId = String(payload.category_id ?? '');
  const question = String(payload.question ?? '').trim();
  const options = Array.isArray(payload.options) ? payload.options.map((x) => String(x).trim()) : [];
  const correctIndex = Number(payload.correct_index);
  const explanation = String(payload.explanation ?? '').trim() || null;
  const sourceUrl = String(payload.source_url ?? '').trim() || null;

  if (!categoryId || question.length < 10 || options.length !== 4 || options.some((x) => !x) ||
      new Set(options.map(normalizeTurkishText)).size !== 4 || !Number.isInteger(correctIndex) || correctIndex < 0 || correctIndex > 3) {
    return json({ error: 'validation_failed' }, 422);
  }

  const { data: previous } = await adminClient
    .from('question_submissions')
    .select('id, status, created_at')
    .eq('author_id', user.id)
    .eq('client_request_id', clientRequestId)
    .maybeSingle();
  if (previous) {
    return json({
      submission_id: previous.id,
      status: previous.status,
      duplicate_candidates: 0,
      idempotent_replay: true,
    }, 200);
  }

  const normalizedQuestion = normalizeTurkishText(question);
  const normalizedHash = await sha256(normalizedQuestion);
  const fingerprint = await sha256(questionFingerprintInput(question, options, correctIndex));

  const [{ data: exactPublished, error: publishedError }, { data: exactPending, error: pendingError }] = await Promise.all([
    adminClient.from('questions').select('id').eq('normalized_hash', normalizedHash).limit(5),
    adminClient.from('question_submissions').select('id').neq('status', 'rejected').neq('status', 'archived').eq('normalized_hash', normalizedHash).limit(5),
  ]);
  if (publishedError || pendingError) return json({ error: 'duplicate_check_failed' }, 500);

  const { data: submission, error: insertError } = await adminClient
    .from('question_submissions')
    .insert({
      author_id: user.id,
      category_id: categoryId,
      question_text: question,
      options,
      correct_index: correctIndex,
      explanation,
      source_url: sourceUrl,
      normalized_hash: normalizedHash,
      fingerprint,
      client_request_id: clientRequestId,
      status: 'pending_review',
    })
    .select('id, status, created_at')
    .single();

  if (insertError || !submission) return json({ error: 'submission_failed' }, 500);

  const existing = exactPublished ?? [];
  if (existing.length) {
    const { error } = await adminClient.from('question_duplicate_candidates').insert(
      existing.map((match) => ({
        submission_id: submission.id,
        existing_question_id: match.id,
        similarity: 1,
        method: 'exact_hash',
      })),
    );
    if (error) return json({ error: 'duplicate_record_failed' }, 500);
  }

  const pending = (exactPending ?? []).filter((row) => row.id !== submission.id);
  if (pending.length) {
    const { error } = await adminClient.from('submission_duplicate_candidates').insert(
      pending.map((match) => ({
        submission_id: submission.id,
        duplicate_submission_id: match.id,
        similarity: 1,
        method: 'exact_hash',
      })),
    );
    if (error) return json({ error: 'pending_duplicate_record_failed' }, 500);
  }

  return json({
    submission_id: submission.id,
    status: submission.status,
    duplicate_candidates: existing.length + pending.length,
    idempotent_replay: false,
  }, 201);
});
