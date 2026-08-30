# Build & Delivery Status

## Product rule
The player app is permanently free and ad-free. There is no subscription, IAP, paid booster, or advertising SDK requirement.

## Current delivery gate
The repository currently contains a working Android prototype plus backend-foundation work. It is **not** a store-ready production application yet.

## Required production gates
1. Backend migration applied and verified on the real staging Supabase project.
2. Player authentication and real submission API connected end-to-end.
3. Admin authentication/MFA and private review API connected end-to-end.
4. Server-authoritative solo match flow replaces local simulation.
5. Duel state machine is server-authoritative and reconnect-safe.
6. Realtime multiplayer and matchmaking are validated under failure/reconnect tests.
7. RLS, rate limits, abuse controls, anti-cheat and audit logging are production-hardened.
8. Question bank is populated, moderated, duplicate-resistant and reportable.
9. iOS client is implemented against the same contracts.
10. Closed beta passes release gates; Android/iOS production builds and store metadata are ready.

## Current blocker
The repository has Supabase migration/function source, but this environment does not have a connected Supabase project deployment credential. Therefore real migration execution cannot honestly be marked complete.
