import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

const cors = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Content-Type": "application/json",
};

const respond = (body: unknown, status = 200) =>
  new Response(JSON.stringify(body), { status, headers: cors });

Deno.serve(async (req) => {
  if (req.method === "OPTIONS") return new Response("ok", { headers: cors });
  if (req.method !== "POST") return respond({ error: "method_not_allowed" }, 405);

  const authorization = req.headers.get("Authorization");
  if (!authorization?.startsWith("Bearer ")) return respond({ error: "unauthorized" }, 401);

  const url = Deno.env.get("SUPABASE_URL");
  const anon = Deno.env.get("SUPABASE_ANON_KEY");
  const service = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
  if (!url || !service) return respond({ error: "server_not_configured" }, 500);

  const authClient = createClient(url, anon ?? service, {
    global: { headers: { Authorization: authorization } },
  });
  const db = createClient(url, service);

  const { data: { user }, error: userError } = await authClient.auth.getUser();
  if (userError || !user) return respond({ error: "unauthorized" }, 401);

  const body = await req.json().catch(() => ({}));
  const mode = body.mode === "duel" ? "duel" : "solo";
  const requestedCount = Number.isInteger(body.question_count) ? body.question_count : 10;
  const questionCount = Math.min(20, Math.max(5, requestedCount));

  const { data: questions, error: questionError } = await db
    .from("questions")
    .select("id")
    .eq("is_published", true)
    .limit(500);

  if (questionError) return respond({ error: "question_bank_unavailable" }, 503);
  if (!questions || questions.length < questionCount) {
    return respond({ error: "insufficient_question_bank" }, 409);
  }

  const questionIds = [...questions]
    .sort(() => Math.random() - 0.5)
    .slice(0, questionCount)
    .map((q) => q.id);

  const { data: match, error: matchError } = await db
    .from("matches")
    .insert({
      mode,
      player_a: user.id,
      question_ids: questionIds,
      status: mode === "solo" ? "active" : "waiting",
    })
    .select("id, mode, status, question_ids, current_index, player_a_score, player_b_score, started_at, expires_at")
    .single();

  if (matchError || !match) return respond({ error: "match_create_failed" }, 500);
  return respond({ match }, 201);
});
