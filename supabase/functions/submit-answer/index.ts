import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

const headers = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Content-Type": "application/json",
};

Deno.serve(async (req) => {
  if (req.method === "OPTIONS") return new Response("ok", { headers });

  const authorization = req.headers.get("Authorization");
  if (!authorization) return new Response(JSON.stringify({ error: "unauthorized" }), { status: 401, headers });

  const userClient = createClient(
    Deno.env.get("SUPABASE_URL")!,
    Deno.env.get("SUPABASE_ANON_KEY")!,
    { global: { headers: { Authorization: authorization } } },
  );
  const serviceClient = createClient(
    Deno.env.get("SUPABASE_URL")!,
    Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
  );

  const { data: userData, error: userError } = await userClient.auth.getUser();
  const user = userData.user;
  if (userError || !user) return new Response(JSON.stringify({ error: "unauthorized" }), { status: 401, headers });

  const body = await req.json().catch(() => ({}));
  const matchId = typeof body.match_id === "string" ? body.match_id : "";
  const selectedIndex = Number.isInteger(body.selected_index) ? body.selected_index : -1;
  const clientElapsed = Number.isInteger(body.elapsed_ms) ? Math.max(0, body.elapsed_ms) : 0;

  if (!matchId || selectedIndex < 0 || selectedIndex > 3) {
    return new Response(JSON.stringify({ error: "invalid_request" }), { status: 400, headers });
  }

  const { data: match } = await serviceClient
    .from("matches")
    .select("id, status, player_a, player_b, question_ids, current_index, expires_at")
    .eq("id", matchId)
    .single();

  if (!match || match.status !== "active" || (match.player_a !== user.id && match.player_b !== user.id)) {
    return new Response(JSON.stringify({ error: "match_unavailable" }), { status: 409, headers });
  }

  if (new Date(match.expires_at).getTime() <= Date.now()) {
    await serviceClient.from("matches").update({ status: "expired" }).eq("id", matchId);
    return new Response(JSON.stringify({ error: "match_expired" }), { status: 409, headers });
  }

  const questionId = match.question_ids[match.current_index];
  const { data: question } = await serviceClient
    .from("questions")
    .select("id, options, correct_index")
    .eq("id", questionId)
    .eq("status", "published")
    .single();

  if (!question) return new Response(JSON.stringify({ error: "question_unavailable" }), { status: 503, headers });

  const effectiveElapsed = Math.min(15000, Math.max(0, clientElapsed));
  const correct = selectedIndex === question.correct_index;
  const points = correct
    ? 100 + Math.max(0, 50 - Math.min(50, Math.floor(effectiveElapsed / 300)))
    : 0;

  const { error: answerError } = await serviceClient.from("match_answers").insert({
    match_id: matchId,
    player_id: user.id,
    question_id: questionId,
    selected_index: selectedIndex,
    elapsed_ms: effectiveElapsed,
    awarded_points: points,
    is_correct: correct,
  });

  if (answerError) {
    if (answerError.code === "23505") return new Response(JSON.stringify({ error: "answer_already_submitted" }), { status: 409, headers });
    return new Response(JSON.stringify({ error: "answer_record_failed" }), { status: 500, headers });
  }

  const scoreColumn = user.id === match.player_a ? "player_a_score" : "player_b_score";
  const currentScore = user.id === match.player_a ? 0 : 0;
  // Atomic score increment is handled with a small SQL RPC in production.
  // The returned points are never trusted from the client.
  const { data: advanced } = await serviceClient.rpc("advance_match_after_answer", { p_match_id: matchId });

  return new Response(JSON.stringify({
    question_id: questionId,
    correct,
    awarded_points: points,
    advanced,
    current_score: currentScore,
  }), { status: 200, headers });
});
