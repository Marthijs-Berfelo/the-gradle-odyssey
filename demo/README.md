# Odyssey demo — stage worktrees

Each of the 5 talk stages is a git tag; this folder's scripts turn those tags into
separate, independent working directories (git worktrees) so a live-coding mistake in
one stage never touches another.

## Prerequisites

Each stage is a Spring Boot app that needs a Java 25 toolchain and a running Docker
daemon (Testcontainers spins up Postgres on demand — no manual database setup).

```bash
# Java 25 toolchain is installed and discoverable
/usr/libexec/java_home -v 25

# Docker is running
docker info --format '{{.ServerVersion}}'
```

If the Java check fails, install a JDK 25 (e.g. via `sdkman` or your platform's
package manager) — Gradle's toolchain support will pick it up automatically once
`java_home` can find it. If the Docker check errors instead of printing a version,
start Docker Desktop before continuing; every stage's tests and `bootTestRun` depend
on it.

## One-time setup

Add this to your shell rc (`~/.zshrc` or `~/.bashrc`):

```bash
source /path/to/the-gradle-odyssey/demo/scripts/_open-stage-completions.bash

stage() {
  cd "$(/path/to/the-gradle-odyssey/demo/scripts/open-stage.sh "$1")"
}
complete -F _open_stage_completions stage
```

Then: `demo/scripts/checkout-stages.sh` (rerun any time to pick up new/updated tags).

### Opening stages in your IDE

Each worktree is an independent directory, but most IDEs (IntelliJ IDEA included)
bind one window to one project root — `cd`-ing via `stage <name>` won't retarget an
already-open window. Before the talk, open every stage's worktree as its own IDE
window:

```bash
demo/scripts/checkout-stages.sh
idea demo/.worktrees/01-ithaca
idea demo/.worktrees/02-setting-sail
idea demo/.worktrees/03-cyclops-cave
idea demo/.worktrees/04-sirens
idea demo/.worktrees/05-ithaca-regained
```

During the talk, switch stages with Cmd+Tab between the pre-opened windows. The
terminal `stage <name>` function still works independently for `gradlew`/`curl` —
it's just not what retargets the IDE.

## During the talk

```bash
stage 01-ithaca            # <TAB> completes stage names
./gradlew bootTestRun
```

If a live edit breaks something mid-stage, just `stage <next-stage-name>` — the next
stage's worktree is untouched.

## Interacting with the API

Each stage's `<app>/client/` folder holds IntelliJ HTTP Client request files, one per
API (`heroes.http`, `monsters.http`, `encounters.http`), plus an
`http-client.env.json` with the `dev` environment's base URLs. Open any `.http` file
in IDEA, select the `dev` environment, and click the gutter run icon per request —
`heroes.http` and `monsters.http` capture the created id into `heroId`/`monsterId`
globals that `encounters.http` reuses.

## Verifying a stage from scratch

To confirm a stage's worktree is fully working — useful right after `checkout-stages.sh`,
or before walking on stage:

```bash
stage 02-setting-sail
./gradlew test                          # full suite across all four modules
./gradlew :heroes-service:bootTestRun &
./gradlew :monsters-service:bootTestRun &
./gradlew :encounters-service:bootTestRun &

# readiness only turns UP once Flyway has migrated and the R2DBC pool is live
until curl -s localhost:8081/actuator/health/readiness | grep -q '"status":"UP"'; do sleep 1; done
until curl -s localhost:8082/actuator/health/readiness | grep -q '"status":"UP"'; do sleep 1; done
until curl -s localhost:8083/actuator/health/readiness | grep -q '"status":"UP"'; do sleep 1; done

curl -s -X POST localhost:8081/heroes -H 'Content-Type: application/json' \
  -d '{"name":"Odysseus","epithet":"the Cunning","strength":9}'
curl -s -X POST localhost:8082/monsters -H 'Content-Type: application/json' \
  -d '{"name":"Polyphemus","domain":"cave","danger":7}'
curl -s -X POST localhost:8083/encounters -H 'Content-Type: application/json' \
  -d '{"heroId":1,"monsterId":1}'
# -> {"heroId":1,"monsterId":1,"outcome":"hero wins"}

open http://localhost:8081/docs  # Swagger UI, repeat on 8082/8083 as needed
kill %1 %2 %3                    # stop all three bootTestRun processes
```

### Gradle caches and stale state

Once a stage's build is multi-module, both of Gradle's caches become more visible during
rehearsal/live-coding and need a documented reset path so a stale cache doesn't produce a
confusing on-stage result:

- **Build cache** (task output cache, local — `~/.gradle/caches/build-cache-1/`): if enabled
  (`org.gradle.caching=true` in `gradle.properties`, not currently set in this repo), reset
  with `./gradlew clean --no-build-cache` for a one-off run, or delete the cache directory
  outright (`rm -rf ~/.gradle/caches/build-cache-1`) to force every module to rebuild from
  scratch.
- **Configuration cache** (`org.gradle.configuration-cache=true`, not currently set either):
  reset with `./gradlew <task> --no-configuration-cache` for a one-off run, or delete the
  stored entries (`rm -rf <stage-dir>/.gradle/configuration-cache`) to force reconfiguration.
  Any `buildSrc` change invalidates the whole build's configuration cache — relevant from
  Stage 3 onward, where a presenter hitting a stale/invalid cache mid-demo should know this
  is the fix, not a sign something is broken.

Neither flag is turned on in this repo's `gradle.properties` as of Stage 2.
