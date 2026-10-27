# Stage 3 — The Cyclops's Cave: Live-Coding Cue Card

Condensed from `.claude/plans/2026-10-27-stage-3-cyclops-cave.md` (Phase 1 only).

---

## Task 1 — Scaffold `buildSrc`

```bash
cd demo/odyssey-app
mkdir -p buildSrc/src/main/kotlin
```

`demo/odyssey-app/buildSrc/build.gradle.kts`:

```kotlin
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
    implementation("org.jetbrains.kotlin:kotlin-allopen:2.3.21")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:4.1.1")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.2.0")
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.25.0")
}
```

Verify: `./gradlew :buildSrc:compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 2 — `gr.odyssey.spring-service` convention plugin

`buildSrc/src/main/kotlin/gr.odyssey.spring-service.gradle.kts` — full body at plan Task 2
Step 1. Verify: `./gradlew :buildSrc:compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 3 — `gr.odyssey.openapi-codegen` convention plugin + parameterized function

`buildSrc/src/main/kotlin/gr.odyssey.openapi-codegen.gradle.kts` — slim, just the three
plugins plus a `repositories { mavenCentral() }` block and the shared `-Xjsr305=strict`/
`-Xannotation-default-target=param-property` compiler flags (plan Task 3 Step 1).
`buildSrc/src/main/kotlin/ApiGeneration.kt` — `configureApiGeneration()` as a `Project`
extension function with defaulted params for spec path, output dirs, and
generateServer/generateClient toggles (plan Task 3 Step 2). Verify:
`./gradlew :buildSrc:compileKotlin --configure-on-demand` → `BUILD SUCCESSFUL`.

---

## Task 4 — Migrate the three services

```bash
cd demo/odyssey-app
```

Rewrite `heroes-service/build.gradle.kts` to the slim form (plan Task 4 Step 1), then:

```bash
cp heroes-service/build.gradle.kts monsters-service/build.gradle.kts
```

Rewrite `encounters-service/build.gradle.kts` to its own slim form (plan Task 4 Step 3 —
same shape, WebClient/WireMock deps instead of R2DBC/Flyway/Postgres).

Verify: `./gradlew :heroes-service:compileKotlin :monsters-service:compileKotlin :encounters-service:compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 5 — Migrate `odyssey-api`

Rewrite `odyssey-api/build.gradle.kts` to apply `gr.odyssey.openapi-codegen` and call
`configureApiGeneration()` (plan Task 5 Step 1) — delete the old in-module function
definition, keep the call.

Verify: `./gradlew :odyssey-api:compileKotlin` → `BUILD SUCCESSFUL`, regenerates
`HeroApi.kt`/`MonsterApi.kt`/`EncounterApi.kt` under `build/generated/api/{server,client}`.

---

## Task 6 — Full-build verification

```bash
cd demo/odyssey-app
./gradlew test   # BUILD SUCCESSFUL across all four modules

./gradlew :heroes-service:bootTestRun &
./gradlew :monsters-service:bootTestRun &
./gradlew :encounters-service:bootTestRun &

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

kill %1 %2 %3
```

**Payoff moment:** edit the single `implementation("org.jlleitschuh.gradle:ktlint-gradle:...")`
line in `buildSrc/build.gradle.kts` — every one of the three services picks up the new
version on next build. Contrast live with Stage 2's "fix it four times" Cons bullet.

---

**HARD BREAK — rehearse all of the above end-to-end before Phase 2 (squash commit + tag
`stage-3-cyclops-cave`, see the plan file).**
