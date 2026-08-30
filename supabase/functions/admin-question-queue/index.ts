import { createClient } from 'npm:@supabase/supabase-js@2';

const json = (body: unknown, status = 200) => new Response(JSON.stringify(body), {
  status,
  headers: { 'Content-Type': 'application/json' },
});

Deno.serve(async (req) => {
  if (req.method !== 'GET') return json({ error: 'method_not_allowed' }, 405);

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

  const { data: role, error: roleError } = await adminClient
    .from('admin_roles')
    .select('role, is_active')
    .eq('user_id', user.id)
    .maybeSingle();
  if (roleError || !role?.is_active || !['admin', 'moderator', 'content_editor'].includes(role.role)) {
    return json({ error: 'forbidden' }, 403);
  }

  const status = new URL(req.url).searchParams.get('status') ?? 'pending_review';
  const limit = Math.min(Number(new URL(req.url).searchParams.get('limit') ?? 50), 100);

  const { data, error } = await adminClient
    .from('question_submissions')
    .select('id, category_id, question_text, options, correct_index, explanation, source_url, status, author_id, created_at')
    .eq('status', status)
    .order('created_at', { ascending: true })
    .limit(limit);
  if (error) return json({ error: 'queue_failed' }, 500);

  const ids = (data ?? []).map((x) => x.id);
  const duplicateCounts = new Map<string, number>();
  if (ids.length) {
    const { data: duplicateRows } = await adminClient
      .from('submission_duplicate_candidates')
      .select('submission_id, similarity')
      .in('submission_id', ids)
      .gte('similarity', 0.92);
    for (const row of duplicateRows ?? []) duplicateCounts.set(row.submission_id, (duplicateCounts.get(row.submission_id) ?? 0) + 1);
  }

  return json({
    items: (data ?? []).map((item) => ({
      ...item,
      duplicate_candidates: duplicateCounts.get(item.id) ?? 0,
    })),
  });
});
