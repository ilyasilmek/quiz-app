# Release Gates

A build is not releaseable until all gates pass:

1. Player build succeeds.
2. Admin build succeeds independently.
3. Player boundary check passes.
4. Unit and UI tests pass.
5. No player source references private admin endpoints or service-role credentials.
6. No secrets are committed.
7. Debug placeholders are rejected for production builds.
8. Backend migrations pass against a disposable database.
9. Submission flow passes validation, duplicate and moderation integration tests.
10. Multiplayer results are server-authoritative.
11. Crash-free beta and critical-path smoke tests pass.
12. Store privacy/permission declarations match actual SDK usage.
