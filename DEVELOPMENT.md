# Development guide

## Branching

| Branch | Purpose |
|---|---|
| `main` | Milestone releases only. Nothing is committed here directly. |
| `develop` | Integration branch. Every feature merges here first. |
| `feature/<issue>-<short-name>` | One branch per feature or issue, created from `develop`. |

Other prefixes follow the same rule (branch from `develop`, merge back into `develop`): `fix/`, `docs/`, `chore/`.

### Day-to-day flow

1. Pick an issue from the current milestone.
2. Create a branch from an up-to-date `develop`:
   ```bash
   git checkout develop && git pull
   git checkout -b feature/12-seat-hold-api
   ```
3. Commit in small steps and push the branch.
4. Open a pull request into **`develop`** (not `main`), reference the issue (`Closes #12`), and make sure CI passes.
5. Merge after review, then delete the feature branch.

### Milestone release

When a milestone is complete (M1 POC, M2 Core platform, M3 Scale and AI):

1. Confirm `develop` is green and the milestone's issues are closed.
2. Open a pull request from `develop` into `main` titled with the milestone name.
3. Merge it and tag the release on `main`, e.g. `v0.1.0` for M1.

## Pull requests

- One issue per pull request, kept small enough to review in one sitting.
- Include tests; integration tests use Testcontainers, so they do not depend on your local Mongo and Kafka.
- Update docs (`README.md`, `docs/architecture.md`) when behaviour or architecture changes.

## Local environment

Local development uses the MongoDB (replica set) and Kafka containers you already run, under the `local` Spring profile. See the "Local development" section of the [README](README.md).
