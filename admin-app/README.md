# Bilgi Arenası Admin

Separate operator application for moderation and game operations.

## Boundary
- Player app: public API only.
- Admin app: private admin API only.
- Admin app is never imported by the player app.

## Planned production controls
- MFA
- Short-lived access tokens
- Role-based permissions
- Audit log
- Device/session risk controls
- Separate private API origin

This prototype intentionally contains no service-role key and no database credentials.
