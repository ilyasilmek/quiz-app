import { createClient } from 'npm:@supabase/supabase-js@2';

const json = (body: unknown, status = 200) => new Response(JSON.stringify(body), {
  status,
  headers: { 'Content-Type': 'application/json' },
});

const allowedActions = new Set(['approve', 'reject', 'publish', 'archive']);

Deno.serve(async (req) => {
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

  const { data: role, error: roleError } = await adminClient
    .from('admin_roles')
    .select('role, is_active')
    .eq('user_id', user.id)
    .maybeSingle();
  if (roleError || !role?.is_active || !['admin', 'moderator', 'content_editor'].includes(role.role)) {
    return json({ error: 'forbidden' }, 403);
  }

  const payload = await req.json().catch(() => null);
  const submissionId = String(payload?.submission_id ?? '');
  const action = String(payload?.action ?? '');
  const rejectionReason = String(payload?.rejection_reason ?? '').trim() || null;
  if (!submissionId || !allowedActions.has(action)) return json({ error: 'validation_failed' }, 422);

  const { data: submission, error: submissionError } = await adminClient
    .from('question_submissions')
    .select('*')
    .eq('id', submissionId)
    .single();
  if (submissionError || !submission) return json({ error: 'submission_not_found' }, 404);

  if (action === 'approve' && !['pending_review'].includes(submission.status)) {
    return json({ error: 'invalid_state_transition', status: submission.status }, 409);
  }
  if (action === 'publish' && submission.status !== 'approved') {
    return json({ error: 'publish_requires_approval', status: submission.status }, 409);
  }

  if (['publish', 'approve'].includes(action)) {
    const [{ data: publishedDuplicates }, { data: pendingDuplicates }] = await Promise.all([
      adminClient
        .from('question_duplicate_candidates')
        .select('existing_question_id, similarity, method')
        .eq('submission_id', submissionId)
        .gte('similarity', 0.92),
      adminClient
        .from('submission_duplicate_candidates')
        .select('duplicate_submission_id, similarity, method')
        .eq('submission_id', submissionId)
        .gte('similarity', 0.92),
    ]);
    const duplicates = [
      ...(publishedDuplicates ?? []).map((d) => ({ ...d, scope: 'published' })),
      ...(pendingDuplicates ?? []).map((d) => ({ ...d, scope: 'pending_submission' })),
    ];
    if (duplicates.length) {
      return json({ error: 'duplicate_review_required', duplicates }, 409);
    }
  }

  if (action === 'publish') {
    const { data: question, error: questionError } = await adminClient
      .from('questions')
      .insert({
        category_id: submission.category_id,
        question_text: submission.question_text,
        options: submission.options,
        correct_index: submission.correct_index,
        explanation: submission.explanation,
        source_url: submission.source_url,
        author_id: submission.author_id,
        normalized_hash: submission.normalized_hash,
        fingerprint: submission.fingerprint,
        is_published: true,
      })
      .select('id')
      .single();
    if (questionError || !question) return json({ error: 'publish_failed' }, 500);

    const { error: submissionUpdateError } = await adminClient.from('question_submissions').update({
      status: 'published',
      reviewed_by: user.id,
      reviewed_at: new Date().toISOString(),
    }).eq('id', submissionId);
    if (submissionUpdateError) return json({ error: 'submission_state_update_failed' }, 500);

    await adminClient.from('admin_audit_log').insert({
      admin_id: user.id,
      action: 'publish',
      entity_type: 'question_submission',
      entity_id: submissionId,
      metadata: { published_question_id: question.id },
    });
    return json({ ok: true, status: 'published', question_id: question.id });
  }

  const nextStatus = action === 'approve' ? 'approved' : action === 'reject' ? 'rejected' : 'archived';
  const { error: updateError } = await adminClient.from('question_submissions').update({
    status: nextStatus,
    rejection_reason: action === 'reject' ? rejectionReason : null,
    reviewed_by: user.id,
    reviewed_at: new Date().toISOString(),
  }).eq('id', submissionId);
  if (updateError) return json({ error: 'review_failed' }, 500);

  await adminClient.from('admin_audit_log').insert({
    admin_id: user.id,
    action,
    entity_type: 'question_submission',
    entity_id: submissionId,
    metadata: { rejection_reason: rejectionReason },
  });

  return json({ ok: true, status: nextStatus });
});
