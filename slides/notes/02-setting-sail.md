# Stage 2 — Setting Sail: Live-Coding Cue Card

Condensed from `.claude/plans/2026-10-04-stage-2-setting-sail.md` (Phase 1 only). Snippet
sources referenced below live under `slides/snippets/02-setting-sail/`.

---

## Task 1 — Multi-module root

`demo/odyssey-app/settings.gradle.kts`:

```kotlin
rootProject.name = "odyssey-app"

include("odyssey-api", "heroes-service", "monsters-service", "encounters-service")
```

```bash
cd demo/odyssey-app
rm build.gradle.kts

mkdir -p odyssey-api/src/main/resources/openapi
mkdir -p heroes-service/src/main/kotlin/gr/odyssey/heroes
mkdir -p heroes-service/src/main/resources/db/migration
mkdir -p heroes-service/src/test/kotlin/gr/odyssey/heroes
mkdir -p monsters-service/src/main/kotlin/gr/odyssey/monsters
mkdir -p monsters-service/src/main/resources/db/migration
mkdir -p monsters-service/src/test/kotlin/gr/odyssey/monsters
mkdir -p encounters-service/src/main/kotlin/gr/odyssey/encounters
mkdir -p encounters-service/src/test/kotlin/gr/odyssey/encounters
```

`client/` stays at the root — one shared `.http` folder, not per-module.

---

## Task 2 — `odyssey-api` module

```bash
cd demo/odyssey-app
./gradlew bootTestRun &
until curl -s localhost:8080/actuator/health/readiness | grep -q '"status":"UP"'; do sleep 1; done
curl -s localhost:8080/v3/api-docs | python3 -m json.tool > /tmp/odyssey-stage1-api-docs.json
kill %1
```

```bash
cp slides/snippets/02-setting-sail/odyssey-api/src/main/resources/openapi/odyssey-api.yaml \
   demo/odyssey-app/odyssey-api/src/main/resources/openapi/odyssey-api.yaml

cp slides/snippets/02-setting-sail/odyssey-api/build.gradle.kts \
   demo/odyssey-app/odyssey-api/build.gradle.kts
```

Verify: `./gradlew :odyssey-api:compileKotlin` → `BUILD SUCCESSFUL`, produces
`HeroApi.kt` + `MonsterApi.kt` + `EncounterApi.kt` under both `generated/server` and
`generated/client`. The spec's schemas all carry explicit `required:` arrays — without
them every generated property is nullable, which breaks the existing non-null domain
code (`hero.strength >= monster.danger`, etc.). Each service implements only its own
generated server interface (`HeroController : HeroApi`, etc.); `encounters-service`
additionally consumes the generated client `HeroApi`/`MonsterApi` (its own client
`EncounterApi` goes unused — accepted tradeoff of one spec for all three domains).

---

## Task 3 — `heroes-service` module

Write `heroes-service/build.gradle.kts` (same shape as Task 5's `encounters-service`
build file but with R2DBC/Flyway/Postgres deps — see plan Task 3 Step 1 for full text).

```bash
cd demo/odyssey-app
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/Hero.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/Hero.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/HeroController.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/HeroController.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/HeroService.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/HeroService.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/HeroEntity.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/HeroEntity.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/HeroRepository.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/HeroRepository.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/HeroNotFoundException.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/HeroNotFoundException.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/DTOMappers.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/DTOMappers.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/hero/DomainMappers.kt heroes-service/src/main/kotlin/gr/odyssey/heroes/DomainMappers.kt
rm odyssey-app/src/main/kotlin/gr/odyssey/app/hero/HeroDto.kt
```

Replace `package gr.odyssey.app.hero` → `package gr.odyssey.heroes` in all of the above
(`HeroDto.kt` is deleted, not moved — its types now come from `odyssey-api`'s generated
`gr.odyssey.api.server.model`). Rewrite `HeroController.kt` to implement `HeroApi`
instead of hand-rolled `@GetMapping`/`@PostMapping` — same `runCatching { }.recover { }`
404 shape, each single-item result wrapped in `ResponseEntity.ok(...)`:

```kotlin
@RestController
class HeroController(
    private val heroService: HeroService,
) : HeroApi {
    override suspend fun listHeroes(): Flow<HeroDTO> = ...
    override suspend fun getHeroById(id: Long): ResponseEntity<HeroDTO> = ...
    override suspend fun createHero(heroRequestDTO: HeroRequestDTO): ResponseEntity<HeroDTO> = ...
}
```

Rewrite `DTOMappers.kt` to import `HeroDTO`/`HeroRequestDTO` from
`gr.odyssey.api.server.model` instead of the deleted `HeroDto.kt`. Full bodies are
checked in at `slides/snippets/02-setting-sail/heroes-service/src/main/kotlin/gr/odyssey/heroes/`.

```bash
git mv odyssey-app/src/main/resources/db/migration/V20261001120000__create_heroes.sql heroes-service/src/main/resources/db/migration/V20261001120000__create_heroes.sql
git mv odyssey-app/src/main/resources/db/migration/V20261001120001__seed_heroes.sql heroes-service/src/main/resources/db/migration/V20261001120001__seed_heroes.sql
```

`heroes-service/src/main/resources/application.yaml`:

```yaml
server:
  port: 8081

spring:
  application:
    name: heroes-service

springdoc:
  swagger-ui:
    path: /docs
```

`heroes-service/src/main/kotlin/gr/odyssey/heroes/HeroesServiceApplication.kt`:

```kotlin
package gr.odyssey.heroes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HeroesServiceApplication

fun main(args: Array<String>) {
    runApplication<HeroesServiceApplication>(*args)
}
```

```bash
git mv odyssey-app/src/test/kotlin/gr/odyssey/app/TestcontainersConfiguration.kt heroes-service/src/test/kotlin/gr/odyssey/heroes/TestcontainersConfiguration.kt
```

Replace `package gr.odyssey.app` → `package gr.odyssey.heroes` in that file. Add
`heroes-service/src/test/kotlin/gr/odyssey/heroes/TestHeroesServiceApplication.kt`:

```kotlin
package gr.odyssey.heroes

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<HeroesServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
```

```bash
git mv odyssey-app/src/test/kotlin/gr/odyssey/app/hero/HeroControllerTest.kt heroes-service/src/test/kotlin/gr/odyssey/heroes/HeroControllerTest.kt
```

Replace `package gr.odyssey.app.hero` → `package gr.odyssey.heroes`,
`import gr.odyssey.app.TestcontainersConfiguration` → `import gr.odyssey.heroes.TestcontainersConfiguration`,
and add `import gr.odyssey.api.server.model.{HeroDTO,HeroRequestDTO}` (previously
same-package, now resolved from the generated module).

Update `demo/odyssey-app/client/http-client.env.json` → `heroesBase: http://localhost:8081`.

Verify: `./gradlew :heroes-service:test` → `BUILD SUCCESSFUL`.

---

## Task 4 — `monsters-service` module

Mirror Task 3 exactly: `monster`/`Monster` → `gr.odyssey.monsters`, port `8082`.
`MonsterDto.kt` is deleted (not moved), same as `HeroDto.kt`.

```bash
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/Monster.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/Monster.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/MonsterController.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/MonsterController.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/MonsterService.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/MonsterService.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/MonsterEntity.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/MonsterEntity.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/MonsterRepository.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/MonsterRepository.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/MonsterNotFoundException.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/MonsterNotFoundException.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/DTOMappers.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/DTOMappers.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/monster/DomainMappers.kt monsters-service/src/main/kotlin/gr/odyssey/monsters/DomainMappers.kt
rm odyssey-app/src/main/kotlin/gr/odyssey/app/monster/MonsterDto.kt

git mv odyssey-app/src/main/resources/db/migration/V20261001120100__create_monsters.sql monsters-service/src/main/resources/db/migration/V20261001120100__create_monsters.sql
git mv odyssey-app/src/main/resources/db/migration/V20261001120101__seed_monsters.sql monsters-service/src/main/resources/db/migration/V20261001120101__seed_monsters.sql
```

`monsters-service/src/main/resources/application.yaml` → port `8082`, name
`monsters-service`, same `springdoc` block as heroes.

Rewrite `MonsterController.kt` to implement `MonsterApi` (same shape as Hero's
conversion); rewrite `DTOMappers.kt` to import from `gr.odyssey.api.server.model`.
Full bodies checked in at
`slides/snippets/02-setting-sail/monsters-service/src/main/kotlin/gr/odyssey/monsters/`.

`MonstersServiceApplication.kt` / `TestcontainersConfiguration.kt` /
`TestMonstersServiceApplication.kt` / `MonsterControllerTest.kt` — same pattern as
Task 3, substituting `Monsters`/`monsters` (test gets the same
`gr.odyssey.api.server.model.{MonsterDTO,MonsterRequestDTO}` import addition).

Update `http-client.env.json` → `monstersBase: http://localhost:8082`.

Verify: `./gradlew :monsters-service:test` → `BUILD SUCCESSFUL`.

---

## Task 5 — `encounters-service` module (the payoff)

```bash
cp slides/snippets/02-setting-sail/encounters-service/build.gradle.kts \
   demo/odyssey-app/encounters-service/build.gradle.kts
```

```bash
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/encounter/EncounterController.kt encounters-service/src/main/kotlin/gr/odyssey/encounters/EncounterController.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/encounter/EncounterOutcome.kt encounters-service/src/main/kotlin/gr/odyssey/encounters/EncounterOutcome.kt
git mv odyssey-app/src/main/kotlin/gr/odyssey/app/encounter/EncounterService.kt encounters-service/src/main/kotlin/gr/odyssey/encounters/EncounterService.kt
rm odyssey-app/src/main/kotlin/gr/odyssey/app/encounter/EncounterDto.kt
```

Replace `package gr.odyssey.app.encounter` → `package gr.odyssey.encounters` in all
three (`EncounterDto.kt` is deleted, not moved — its model types now come from
`odyssey-api`'s generated `gr.odyssey.api.server.model`).

Rewrite `EncounterController.kt` to implement `EncounterApi` — same pattern as
Hero/Monster, but the 404 catch changes from `HeroNotFoundException`/
`MonsterNotFoundException` (which don't exist here anymore — Hero/Monster lookups are
now HTTP calls) to `WebClientResponseException.NotFound`:

```kotlin
@RestController
class EncounterController(
    private val encounterService: EncounterService,
) : EncounterApi {
    override suspend fun resolveEncounter(encounterRequestDTO: EncounterRequestDTO): ResponseEntity<EncounterDTO> =
        runCatching {
            encounterService
                .resolve(encounterRequestDTO.heroId, encounterRequestDTO.monsterId)
                .toDTO()
        }.recover {
            when (it) {
                is WebClientResponseException.NotFound -> {
                    throw ResponseStatusException(HttpStatus.NOT_FOUND, it.message, it)
                }
                else -> throw it
            }
        }.getOrThrow()
            .let { ResponseEntity.ok(it) }
}
```

Add `DTOMappers.kt` (`EncounterOutcome.toDTO()`, importing the generated
`EncounterDTO`) to replace the mapping function the deleted `EncounterDto.kt` carried.
`EncounterOutcome.kt` itself is unchanged — it stays an internal domain type, mapped to
the generated DTO only at the controller boundary.

Rewrite `EncounterService.kt` body (calls generated clients concurrently; `hero.id`/
`monster.id` are non-null now that the spec marks them `required:`, so the Stage 1
`requireNotNull(...)` calls are gone):

```kotlin
package gr.odyssey.encounters

import gr.odyssey.api.client.api.HeroApi
import gr.odyssey.api.client.api.MonsterApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class EncounterService(
    private val heroApi: HeroApi,
    private val monsterApi: MonsterApi,
) {
    suspend fun resolve(
        heroId: Long,
        monsterId: Long,
    ): EncounterOutcome =
        coroutineScope {
            val heroDeferred = async { heroApi.getHeroById(heroId) }
            val monsterDeferred = async { monsterApi.getMonsterById(monsterId) }

            val hero = heroDeferred.await()
            val monster = monsterDeferred.await()

            EncounterOutcome(
                heroId = hero.id,
                monsterId = monster.id,
                heroWins = hero.strength >= monster.danger,
            )
        }
}
```

```bash
cp slides/snippets/02-setting-sail/encounters-service/src/main/kotlin/gr/odyssey/encounters/ServicesProperties.kt \
   demo/odyssey-app/encounters-service/src/main/kotlin/gr/odyssey/encounters/ServicesProperties.kt
cp slides/snippets/02-setting-sail/encounters-service/src/main/kotlin/gr/odyssey/encounters/HttpClientConfiguration.kt \
   demo/odyssey-app/encounters-service/src/main/kotlin/gr/odyssey/encounters/HttpClientConfiguration.kt
cp slides/snippets/02-setting-sail/encounters-service/src/main/resources/application.yaml \
   demo/odyssey-app/encounters-service/src/main/resources/application.yaml
```

`EncountersServiceApplication.kt`:

```kotlin
package gr.odyssey.encounters

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EncountersServiceApplication

fun main(args: Array<String>) {
    runApplication<EncountersServiceApplication>(*args)
}
```

```bash
cp slides/snippets/02-setting-sail/client/http-client.env.json \
   demo/odyssey-app/client/http-client.env.json

rm -rf odyssey-app/src/main/kotlin/gr/odyssey/app/hero
rm -rf odyssey-app/src/main/kotlin/gr/odyssey/app/monster
rm -rf odyssey-app/src/main/kotlin/gr/odyssey/app/encounter
rm -f odyssey-app/src/main/kotlin/gr/odyssey/app/OdysseyApplication.kt
rm -f odyssey-app/src/main/resources/application.yaml
rm -rf odyssey-app/src/test
```

---

## Task 6 — WireMock test

```bash
cp slides/snippets/02-setting-sail/encounters-service/src/test/kotlin/gr/odyssey/encounters/EncounterControllerTest.kt \
   demo/odyssey-app/encounters-service/src/test/kotlin/gr/odyssey/encounters/EncounterControllerTest.kt
```

Verify: `./gradlew :encounters-service:test` → `BUILD SUCCESSFUL` (no Docker needed).

---

## Task 7 — Full-build verification

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

---

**HARD BREAK — rehearse all of the above end-to-end before Phase 2 (squash commit + tag
`stage-2-setting-sail`, see the plan file).**
