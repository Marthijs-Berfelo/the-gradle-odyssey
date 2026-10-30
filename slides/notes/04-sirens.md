# Stage 4 — The Sirens: Live-Coding Cue Card

Fully self-contained — no separate implementation plan backs this stage. Run each task
directly from this card, verifying after every step.

Narrative thread (from `slides/notes/00-full-story.adoc`): buildSrc deduplicated build
*logic*, but dependency *versions* are still hardcoded and drifting across the four modules
and `buildSrc` itself. This stage centralizes every version into one
`gradle/libs.versions.toml`, then proves both its value (one-line bump propagates everywhere)
and its limit (the catalog file itself, like `buildSrc`, is still landlocked in this one
repo — "versions aligned, but we're still steering for the same cliffs as before").

The catalog also covers every Spring Boot starter and library already in `heroes-service`,
`monsters-service`, and `encounters-service` — not just the handful that happened to carry an
explicit version string. Most of these are platform-managed (resolved via the
`enforcedPlatform(libs.spring.boot.dependencies)` BOM now applied in every module — see Task
4 — so their catalog entries have no `version`/`version.ref` either — the catalog centralizes
the *coordinate*, not a version that doesn't exist yet to centralize). Grouped into
`[bundles]` by role, this also collapses `heroes-service` and `monsters-service`'s
near-identical dependency blocks down to a handful of bundle references each — a second,
smaller "aha" alongside the version bump.

This stage also folds in a Kotlin 2.3.21 → 2.4.21 bump (Task 1) and a full removal of the
`io.spring.dependency-management` plugin in favor of `enforcedPlatform(...)` everywhere
(Task 4). The Kotlin bump surfaced a latent gap in both convention plugins' JVM-target
wiring, fixed as part of Task 2.

---

## Task 1 — Create the version catalog

```bash
cd demo/odyssey-app
mkdir -p gradle
```

`demo/odyssey-app/gradle/libs.versions.toml`:

```toml
[versions]
kotlin = "2.4.21"
springBoot = "4.1.1"
ktlintGradle = "14.2.0"
openapiGenerator = "7.25.0"
springdocOpenapi = "3.1.0"
swaggerAnnotations = "2.2.54"
wiremockSpringBoot = "4.4.2"

[libraries]
kotlin-gradle-plugin = { module = "org.jetbrains.kotlin:kotlin-gradle-plugin", version.ref = "kotlin" }
kotlin-allopen = { module = "org.jetbrains.kotlin:kotlin-allopen", version.ref = "kotlin" }
spring-boot-gradle-plugin = { module = "org.springframework.boot:spring-boot-gradle-plugin", version.ref = "springBoot" }
ktlint-gradle-plugin = { module = "org.jlleitschuh.gradle:ktlint-gradle", version.ref = "ktlintGradle" }
openapi-generator-gradle-plugin = { module = "org.openapitools:openapi-generator-gradle-plugin", version.ref = "openapiGenerator" }
springdoc-openapi-starter-webflux-ui = { module = "org.springdoc:springdoc-openapi-starter-webflux-ui", version.ref = "springdocOpenapi" }
swagger-annotations = { module = "io.swagger.core.v3:swagger-annotations", version.ref = "swaggerAnnotations" }
spring-boot-dependencies = { module = "org.springframework.boot:spring-boot-dependencies", version.ref = "springBoot" }
wiremock-spring-boot = { module = "org.wiremock.integrations:wiremock-spring-boot", version.ref = "wiremockSpringBoot" }

# Platform-managed (version resolved via the enforcedPlatform(libs.spring.boot.dependencies)
# BOM applied in every module — see Task 4 — hence no version/version.ref here. The catalog
# centralizes the coordinate, not a version that doesn't exist yet to centralize).
spring-boot-starter-actuator = { module = "org.springframework.boot:spring-boot-starter-actuator" }
spring-boot-starter-actuator-test = { module = "org.springframework.boot:spring-boot-starter-actuator-test" }
spring-boot-starter-data-r2dbc = { module = "org.springframework.boot:spring-boot-starter-data-r2dbc" }
spring-boot-starter-data-r2dbc-test = { module = "org.springframework.boot:spring-boot-starter-data-r2dbc-test" }
spring-boot-starter-flyway = { module = "org.springframework.boot:spring-boot-starter-flyway" }
spring-boot-starter-flyway-test = { module = "org.springframework.boot:spring-boot-starter-flyway-test" }
spring-boot-starter-validation = { module = "org.springframework.boot:spring-boot-starter-validation" }
spring-boot-starter-validation-test = { module = "org.springframework.boot:spring-boot-starter-validation-test" }
spring-boot-starter-webflux = { module = "org.springframework.boot:spring-boot-starter-webflux" }
spring-boot-starter-webflux-test = { module = "org.springframework.boot:spring-boot-starter-webflux-test" }
spring-boot-starter-webclient = { module = "org.springframework.boot:spring-boot-starter-webclient" }
spring-boot-starter-webclient-test = { module = "org.springframework.boot:spring-boot-starter-webclient-test" }
spring-boot-testcontainers = { module = "org.springframework.boot:spring-boot-testcontainers" }
reactor-kotlin-extensions = { module = "io.projectreactor.kotlin:reactor-kotlin-extensions" }
kotlin-reflect = { module = "org.jetbrains.kotlin:kotlin-reflect" }
kotlin-test-junit5 = { module = "org.jetbrains.kotlin:kotlin-test-junit5" }
kotlinx-coroutines-reactor = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test" }
spring-jdbc = { module = "org.springframework:spring-jdbc" }
jackson-module-kotlin = { module = "tools.jackson.module:jackson-module-kotlin" }
r2dbc-postgresql = { module = "org.postgresql:r2dbc-postgresql" }
postgresql = { module = "org.postgresql:postgresql" }
flyway-database-postgresql = { module = "org.flywaydb:flyway-database-postgresql" }
testcontainers-junit-jupiter = { module = "org.testcontainers:testcontainers-junit-jupiter" }
testcontainers-r2dbc = { module = "org.testcontainers:testcontainers-r2dbc" }
testcontainers-postgresql = { module = "org.testcontainers:testcontainers-postgresql" }
junit-platform-launcher = { module = "org.junit.platform:junit-platform-launcher" }

[bundles]
api = [
    "spring-boot-starter-webflux",
    "spring-boot-starter-validation",
    "kotlinx-coroutines-reactor",
    "kotlin-reflect",
    "swagger-annotations",
]
common = [
    "spring-boot-starter-actuator",
    "reactor-kotlin-extensions",
    "springdoc-openapi-starter-webflux-ui",
    "jackson-module-kotlin",
]
server = ["spring-boot-starter-data-r2dbc", "spring-boot-starter-flyway", "spring-jdbc"]
client = ["spring-boot-starter-webclient"]
test = [
    "spring-boot-starter-actuator-test",
    "spring-boot-starter-validation-test",
    "spring-boot-starter-webflux-test",
    "kotlin-test-junit5",
    "kotlinx-coroutines-test",
    "testcontainers-junit-jupiter",
]
server-test = [
    "spring-boot-starter-data-r2dbc-test",
    "spring-boot-starter-flyway-test",
    "spring-boot-testcontainers",
    "testcontainers-r2dbc",
    "testcontainers-postgresql",
]
client-test = ["spring-boot-starter-webclient-test", "wiremock-spring-boot"]
```

`api` holds exactly what `odyssey-api` needs (Task 4) — webflux, validation,
kotlinx-coroutines-reactor, kotlin-reflect, swagger-annotations. Because `odyssey-api` declares
these as `implementation(...)` (not `api(...)`), they don't propagate transitively to the three
services, so each service re-declares `libs.bundles.api` alongside its own bundles (Task 3).

`common`/`test` hold the remaining dependencies shared by all three services (actuator,
reactor-kotlin-extensions, springdoc, jackson-module-kotlin, and their test equivalents);
`server`/`server-test` hold the R2DBC/Flyway/Postgres-specific extras used only by
`heroes-service`/`monsters-service`; `client`/`client-test` hold the WebClient/WireMock-specific
extras used only by `encounters-service`. TOML bundle keys with a dash (`server-test`,
`client-test`) get the camelCase accessor `libs.bundles.serverTest` / `libs.bundles.clientTest`
— same dash-to-dot/camel rule as every other catalog accessor.

The Postgres runtime drivers (`r2dbc-postgresql`, `postgresql`, `flyway-database-postgresql`,
all `runtimeOnly`) and `junit-platform-launcher` (`testRuntimeOnly`) stay as individual,
unbundled accessors — bundles apply to a single configuration per declaration, and these sit
in different configurations than the `implementation`/`testImplementation` bundles above, so
grouping them wouldn't save a line.

No `[plugins]` section — every external plugin in this repo is still applied via `buildSrc`'s
convention-plugin classpath, not via a `plugins { id(...) version ... }` block in any project
script. That changes in Stage 5.

No internal BOM, either. The `springBoot` entry above already does a BOM's job: it drives
both the Spring Boot Gradle plugin version (`buildSrc/build.gradle.kts`, Task 2) and the
`spring-boot-dependencies` BOM coordinate version (every module's `enforcedPlatform(...)`,
Task 4) from one place. A separate internal BOM would duplicate that with no module left to
consume it.

---

## Task 2 — Wire the catalog into `buildSrc`

`buildSrc` is a separate, included build — it does **not** automatically inherit the root
project's `gradle/libs.versions.toml`. It needs its own settings file to see it.

`demo/odyssey-app/buildSrc/settings.gradle.kts` (new file):

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
```

Rewrite `demo/odyssey-app/buildSrc/build.gradle.kts`:

```kotlin
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.allopen)
    implementation(libs.spring.boot.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
    implementation(libs.openapi.generator.gradle.plugin)
}
```

Note what's *not* here any more: `io.spring.gradle:dependency-management-plugin` is gone from
this classpath entirely — see Task 4 for why (every module now gets its Spring Boot BOM via
`enforcedPlatform(...)` instead of that plugin).

**Kotlin 2.3.21 → 2.4.21 side effect:** bumping `kotlin` in the catalog surfaces a latent gap
in both convention plugins below. Previously, each plugin set `jvmTarget` only on the
`compileKotlin` task (via `tasks { compileKotlin { compilerOptions { jvmTarget.set(...) } } }`),
leaving `compileTestKotlin` to fall back to the toolchain default. Under Kotlin 2.3 that
default happened to resolve to the same target as the `java {}` block's `targetCompatibility`
(25); Kotlin 2.4 changed that fallback, so `compileTestKotlin` silently drifted to JVM 26 while
`compileTestJava` stayed at 25 — a hard compile failure
(`Inconsistent JVM Target Compatibility Between Java and Kotlin Tasks`) the moment a module has
test sources. The fix: move `jvmTarget.set(...)` into the `kotlin { compilerOptions { } }`
extension block instead, which applies to **every** Kotlin compile task (main and test alike),
not just `compileKotlin`.

Rewrite `demo/odyssey-app/buildSrc/src/main/kotlin/gr.odyssey.spring-service.gradle.kts`:

```kotlin
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaVersion = JavaVersion.VERSION_25

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("org.jlleitschuh.gradle.ktlint")
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaVersion.majorVersion))
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
```

(`io.spring.dependency-management` is also dropped from this plugin's `plugins {}` block —
see Task 4.)

Rewrite `demo/odyssey-app/buildSrc/src/main/kotlin/gr.odyssey.openapi-codegen.gradle.kts`:

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaVersion = JavaVersion.VERSION_25

plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaVersion.majorVersion))
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}
```

(This plugin already had no test sources depending on it directly, but gets the same fix for
consistency — and because `io.spring.dependency-management` is dropped from here too, see
Task 4.)

Verify: `./gradlew :buildSrc:compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 3 — Use the catalog in the three services

None of the version literals needing a catalog entry live inside a precompiled script plugin
(`gr.odyssey.spring-service.gradle.kts`, `gr.odyssey.openapi-codegen.gradle.kts`) or
`ApiGeneration.kt` — both are already version-free (plugins applied by id only). The remaining
literals sit in each module's own regular `build.gradle.kts`, which gets the same direct
type-safe `libs.*` accessor as any project build script — no fallback needed. This task goes
beyond versioned literals, though: every dependency in these three modules — versioned or
not — moves to a catalog bundle reference, which is also what collapses `heroes-service` and
`monsters-service`'s identical dependency blocks down to a few lines each.

Replace the entire `dependencies {}` block in `demo/odyssey-app/heroes-service/build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":odyssey-api"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:3.1.0")
    implementation("org.springframework:spring-jdbc")
    implementation("tools.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-r2dbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-r2dbc")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

with:

```kotlin
dependencies {
    implementation(project(":odyssey-api"))
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
    implementation(libs.bundles.common)
    implementation(libs.bundles.api)
    implementation(libs.bundles.server)
    runtimeOnly(libs.r2dbc.postgresql)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.flyway.database.postgresql)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.serverTest)
    testRuntimeOnly(libs.junit.platform.launcher)
}
```

The `enforcedPlatform(...)` line here — and in `monsters-service`/`encounters-service` below —
is new relative to Stage 3; see Task 4 for why every module now declares it directly rather than
relying on a plugin.

Replace `demo/odyssey-app/monsters-service/build.gradle.kts`'s `dependencies {}` block with the
identical new block above (it was byte-identical to `heroes-service`'s before this edit too).

Replace the entire `dependencies {}` block in
`demo/odyssey-app/encounters-service/build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":odyssey-api"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-webclient")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:3.1.0")
    implementation("tools.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webclient-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.4.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

with:

```kotlin
dependencies {
    implementation(project(":odyssey-api"))
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
    implementation(libs.bundles.common)
    implementation(libs.bundles.api)
    implementation(libs.bundles.client)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.clientTest)
    testRuntimeOnly(libs.junit.platform.launcher)
}
```

Verify: `./gradlew :heroes-service:compileKotlin :monsters-service:compileKotlin :encounters-service:compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 4 — Drop `io.spring.dependency-management`, use `enforcedPlatform(...)` everywhere

Stage 3 relied on the `io.spring.dependency-management` plugin in two places: implicitly in the
three services (via the Spring Boot Gradle plugin's own automatic BOM import) and explicitly in
`odyssey-api` (via a `dependencyManagement { imports { mavenBom(...) } }` block, since
`odyssey-api` doesn't apply the Spring Boot Gradle plugin at all). This task replaces both
mechanisms, in every module, with the plain Gradle platform mechanism:
`implementation(enforcedPlatform(libs.spring.boot.dependencies))`. One mechanism, one catalog
entry (`libs.spring.boot.dependencies`), applied uniformly — no plugin-specific DSL, no
string-interpolated BOM coordinate, no services/`odyssey-api` asymmetry.

Remove `id("io.spring.dependency-management")` from the `plugins {}` block in
`demo/odyssey-app/buildSrc/src/main/kotlin/gr.odyssey.spring-service.gradle.kts` (done above in
Task 2) and from `gr.odyssey.openapi-codegen.gradle.kts` (also done above in Task 2). Remove
`implementation(libs.spring.dependency.management.plugin)` and the corresponding
`springDependencyManagement` version entry from the catalog entirely (done in Task 1/2 — neither
appears in the catalog or `buildSrc`'s dependencies any more).

Edit `demo/odyssey-app/odyssey-api/build.gradle.kts` — remove the `dependencyManagement {}`
block entirely:

```kotlin
    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:4.1.1")
        }
    }
```

and add this to the `dependencies {}` block instead:

```kotlin
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
```

Also change:

```kotlin
    implementation("io.swagger.core.v3:swagger-annotations:2.2.54")
```

to using the `api` bundle (bundled with the rest of `odyssey-api`'s dependencies — see Task 1):

```kotlin
    implementation(libs.bundles.api)
```

`enforcedPlatform(...)` accepts the catalog accessor directly — `libs.spring.boot.dependencies`
is a `Provider<MinimalExternalModuleDependency>`, and Gradle's own guidance is to pass that
provider form rather than unwrap it with `.get()`.

`demo/odyssey-app/odyssey-api/build.gradle.kts`'s full `dependencies {}` block is now:

```kotlin
dependencies {
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
    implementation(libs.bundles.api)
}
```

For `heroes-service`, `monsters-service`, and `encounters-service` (Task 3 above), add the same
`implementation(enforcedPlatform(libs.spring.boot.dependencies))` line to each module's
`dependencies {}` block, replacing what the Spring Boot Gradle plugin used to manage
automatically via its own `io.spring.dependency-management` integration. Each service also adds
`implementation(libs.bundles.api)` — `odyssey-api`'s dependencies are `implementation`-scoped,
not `api`-scoped, so they don't propagate transitively; each service needing those types at
compile time (via `project(":odyssey-api")`) must re-declare the `api` bundle itself.

Verify: `./gradlew :odyssey-api:compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 5 — Full-build verification

```bash
cd demo/odyssey-app
./gradlew test   # BUILD SUCCESSFUL across all four modules
```

**Payoff moment:** bump `springBoot = "4.1.1"` to `springBoot = "4.1.2"` in
`gradle/libs.versions.toml` — one line — then run:

```bash
./gradlew :buildSrc:compileKotlin :odyssey-api:compileKotlin \
  :heroes-service:compileKotlin :monsters-service:compileKotlin :encounters-service:compileKotlin
```

Both the Spring Boot Gradle plugin (via `buildSrc`, propagating to all three services through
`gr.odyssey.spring-service` — this is why their `compileKotlin` is included above, to show the
new version resolves cleanly for them too) and the `spring-boot-dependencies` BOM (via every
module's `enforcedPlatform(...)`, including `odyssey-api`) just moved together. In Stage 3
these were independent edits per module; now they're one. Revert the version back to `4.1.1`
after the demo (`4.1.2` is a real, currently-published patch release, confirmed against Maven
Central — safe to resolve, no need to fake it).

---

## Task 6 — The limitation, shown live

Open `buildSrc/build.gradle.kts` and `gradle/libs.versions.toml` side by side and point out:
copying `demo/odyssey-app` into a second, unrelated repo would require copying both files —
the catalog solved version *drift inside this repo*, not *reuse across repos*. That's next
stage's job.

Transition line (ties to the rehearsal script): "versions aligned — but we're still steering
for the same cliffs as before."
