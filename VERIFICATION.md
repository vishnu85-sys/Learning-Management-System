# Verification Checklist

This project was reviewed in two static verification passes before packaging.

## Pass 1 — Source sanity

- Java source brace balance checked across all main classes.
- No merge-conflict markers found.
- HTML DOM IDs were cross-checked against JavaScript DOM lookups.
- JavaScript syntax checked with Node.js.

## Pass 2 — Application contract / regression checks

- Authentication, dashboard, course, enrollment, progress, health and profile API contracts checked against the frontend.
- Search repository method checked.
- H2 test profile and integration test setup checked.
- Progress persistence regression checked: incomplete progress is not written before `completedAt` is set.
- Access-denied errors are mapped to HTTP 403.
- `target/`, `venv/`, and environment files are ignored by Git.
- Frontend JavaScript syntax checked again.

## Full environment verification

Run this on a machine with Maven, Java 17+, and Node.js available:

```powershell
.\scripts\verify.ps1
```

The script runs the complete Maven test suite and a second frontend syntax check.

> Note: the packaging environment used for this archive does not have Maven/dependency downloads available, so a full Maven build was not executed here. The verification script is included so the final compile/test is reproducible on Windows.
