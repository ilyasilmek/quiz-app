import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

const cors = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Content-Type": "application/json",
};

Deno.serve(async (req) => {
  if (req.method === "OPTIONS") return new Response("ok", { headers: cors });

  const auth = req.headers.get("Authorization");
  if (!auth) return new Response(JSON.stringify({ error: "unauthorized" }), { status: 401, headers: cors });

  const supabase = createClient(
    Deno.env.get("SUPABASE_URL")!,
    Deno.env.get("SUPABASE_ANON_KEY")!,
    { global: { headers: { Authorization: auth } } },
  );

  const { data: userData, error: userError } = await supabase.auth.getUser();
  if (userError || !userData.user) {
    return new Response(JSON.stringify({ error: "unauthorized" }), { status: 401, headers: cors });
  }

  const body = await req.json().catch(() => ({}));
  const mode = body.mode === "duel" ? "duel" : "solo";
  const requestedCount = Number.isInteger(body.question_count) ? body.question_count : 10;
  const questionCount = Math.min(20, Math.max(5, requestedCount));

  const { data: questions, error } = await supabase
    .from("questions")
    .select("id")
    .eq("status", "published")
    .limit(200);

  if (error || !questions || questions.length < questionCount) {
    return new Response(JSON.stringify({ error: "insufficient_question_bank" }), { status: 503, headers: cors });
  }

  const shuffled = [...questions].sort(() => Math.random() - 0.5).slice(0, questionCount);
  const questionIds = shuffled.map((q) => q.id);

  const { data: match, error: matchError } = await supabase
    .from("matches")
    .insert({
      mode,
      player_a: userData.user.id,
      question_ids: questionIds,
      status: "active",
    })
    .select("id, mode, status, question_ids, current_index, started_at, expires_at")
    .single();

  if (matchError || !match) {
    return new Response(JSON.stringify({ error: "match_create_failed" }), { status: 500, headers: cors });
  }

  return new Response(JSON.stringify(match), { status: 201, headers: cors });
});
