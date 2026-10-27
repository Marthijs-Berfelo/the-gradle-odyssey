# Stage 5 — Ithaca Regained: Live-Coding Cue Card

Fully self-contained — no separate implementation plan backs this stage. Run each task
directly from this card, verifying after every step.

Narrative thread (from `slides/notes/00-full-story.adoc`): a version catalog fixed drift
inside this one repo, but "the build logic itself is still steering straight for the
cliffs — trapped in this one repo, with no way to reuse it elsewhere." This stage removes
the repo boundary entirely: `buildSrc`'s two convention plugins are extracted into a
standalone, published Gradle plugin (`build-logic-plugin`), and the version catalog itself
is published as a Maven artifact (`odyssey-catalog`) via Gradle's built-in `version-catalog`
plugin. `heroes-service`, `monsters-service`, `encounters-service`, and `odyssey-api` all
consume both exclusively as published artifacts resolved from `mavenLocal()` — no local
file, no local project reference, no copying. "No copying, no drift — this is build logic
any project can adopt."

**Three deliberate departures from the original design note** (resolved with the user
before and during implementation):

1. **Binary plugin classes, not precompiled script plugins.** Gradle's own publishing docs
   state precompiled script plugins (what `buildSrc`'s `.gradle.kts` files are) are "not
   recommended" to publish directly — convert to a binary `Plugin<Project>` class with
   explicit `gradlePlugin { plugins { create(...) } }` registration first. Both convention
   plugins are rewritten as Kotlin classes in `build-logic-plugin` (Task 3).
2. **`buildSrc` is deleted entirely, including for `odyssey-api`.** The original design note
   only named the three services as adopting the published plugin, leaving `odyssey-api`'s
   `gr.odyssey.openapi-codegen` usage unaddressed. Since both of `buildSrc`'s plugins move
   into `build-logic-plugin`, nothing is left to justify keeping `buildSrc` around — leaving
   even one piece of build logic landlocked would undercut this stage's whole payoff.
   `odyssey-api` switches to the published plugin too (Task 6).
3. **The catalog file moves to the `odyssey-app` project root, not `gradle/libs.versions.toml`.**
   Gradle auto-detects `gradle/libs.versions.toml` at its conventional path as the default
   `libs` catalog. That auto-detection collides ("Multiple 'from' invocations") with the
   explicit `versionCatalogs { create("libs") { from("gr.odyssey:odyssey-catalog:1.0.0") } }`
   declaration this stage adds to `settings.gradle.kts` (Task 5) — Gradle sees two
   definitions of the same `libs` catalog and refuses to pick. Moving the file to
   `demo/odyssey-app/libs.versions.toml` (outside the conventional `gradle/` directory)
   removes the auto-detected candidate entirely, leaving the explicit `from(...)` as the only
   definition.

Two new, independent standalone Gradle builds — `demo/build-logic-plugin/` and
`demo/odyssey-catalog/` — live as siblings to `demo/odyssey-app/`, each with its own
`settings.gradle.kts` and copied Gradle wrapper. Neither is `include()`d by
`odyssey-app/settings.gradle.kts`: that's the whole point — they are consumed exactly as
any other repo would consume them, via `mavenLocal()`, never via `project(":...")`.

Both publish under `gr.odyssey`, version `1.0.0`:

- `gr.odyssey:odyssey-catalog:1.0.0` — the published version catalog.
- `gr.odyssey:build-logic-plugin:1.0.0` — the published plugin (two plugin IDs:
  `gr.odyssey.spring-service`, `gr.odyssey.openapi-codegen`).

The catalog publishes first — `build-logic-plugin` itself consumes the published catalog
for its own buildscript dependency versions (Task 3), so even the plugin project's own build
sources versions from the published artifact, not a local file. This isn't in the original
design note; it's a direct consequence of deleting `buildSrc` and fell out naturally once
there was no local catalog file left for the plugin project to read.

A fourth, smaller departure falls out of binary plugins not applying `kotlin-dsl`: see the
note after Task 3's convention-plugin listings.

---

## Task 1 — Add a `[plugins]` section to the existing catalog

The catalog file (`demo/odyssey-app/gradle/libs.versions.toml`) stays exactly where it is for
now — it moves to the project root in Task 5, once `odyssey-app` no longer reads it
directly. Add one new version and one new section to the existing file.

Edit `demo/odyssey-app/gradle/libs.versions.toml` — add to `[versions]`:

```toml
buildLogicPlugin = "1.0.0"
```

Add a new `[plugins]` section (after `[bundles]`):

```toml
[plugins]
odyssey-spring-service = { id = "gr.odyssey.spring-service", version.ref = "buildLogicPlugin" }
odyssey-openapi-codegen = { id = "gr.odyssey.openapi-codegen", version.ref = "buildLogicPlugin" }
```

TOML keys stay hyphenated (not dot-separated) — that's valid TOML syntax and matches every
other entry already in this file (e.g. `r2dbc-postgresql`). Dots inside a TOML key create
nested tables, which Gradle's catalog parser rejects here. The Kotlin DSL accessor these two
entries generate uses dots instead: `libs.plugins.odyssey.spring.service` and
`libs.plugins.odyssey.openapi.codegen` (Task 6) — one version number (`buildLogicPlugin`) now
governs the plugin's own release version, independent of `kotlin`/`springBoot`/etc.

---

## Task 2 — Scaffold and publish `odyssey-catalog`

```bash
cd demo
mkdir -p odyssey-catalog/gradle
cp odyssey-app/gradlew odyssey-app/gradlew.bat odyssey-catalog/
cp -r odyssey-app/gradle/wrapper odyssey-catalog/gradle/wrapper
```

`demo/odyssey-catalog/settings.gradle.kts` (new file):

```kotlin
rootProject.name = "odyssey-catalog"
```

`demo/odyssey-catalog/build.gradle.kts` (new file):

```kotlin
plugins {
    `version-catalog`
    `maven-publish`
}

group = "gr.odyssey"
version = "1.0.0"

catalog {
    versionCatalog {
        from(files("../odyssey-app/gradle/libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
}
```

No `repositories {}` block needed — `publishToMavenLocal` doesn't resolve anything, it only
writes to `~/.m2/repository`.

```bash
cd odyssey-catalog
./gradlew publishToMavenLocal
ls ~/.m2/repository/gr/odyssey/odyssey-catalog/1.0.0/
```

Verify: the listing shows `odyssey-catalog-1.0.0.toml` (plus `.pom`/`.module` files) —
the version catalog, packaged as a real Maven artifact.

The `from(files("../odyssey-app/gradle/libs.versions.toml"))` path above is temporary — it
points at the file's *original* location. Once Task 5 moves the file to
`demo/odyssey-app/libs.versions.toml`, this path updates to `from(files("../odyssey-app/libs.versions.toml"))`
and the catalog is republished so `odyssey-app` resolves the new content.

---

## Task 3 — Scaffold `build-logic-plugin`, consuming the published catalog for its own deps

```bash
cd ../  # back to demo/
mkdir -p build-logic-plugin/src/main/kotlin/gr/odyssey/buildlogic
mkdir -p build-logic-plugin/gradle
cp odyssey-app/gradlew odyssey-app/gradlew.bat build-logic-plugin/
cp -r odyssey-app/gradle/wrapper build-logic-plugin/gradle/wrapper
cd build-logic-plugin
```

`demo/build-logic-plugin/settings.gradle.kts` (new file):

```kotlin
rootProject.name = "build-logic-plugin"

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from("gr.odyssey:odyssey-catalog:1.0.0")
        }
    }
}
```

This is the same pattern Stage 4 used for `buildSrc/settings.gradle.kts` — except the
source is a published Maven coordinate instead of `files(...)`. `mavenLocal()` must be
listed in `dependencyResolutionManagement.repositories` (not just in `pluginManagement`,
and not just in the project's own `repositories {}` block) because that's specifically
where Gradle resolves version-catalog *artifacts* from.

`demo/build-logic-plugin/build.gradle.kts` (new file):

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version libs.versions.kotlin.get()
    `java-gradle-plugin`
    `maven-publish`
}

group = "gr.odyssey"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.allopen)
    implementation(libs.spring.boot.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
    implementation(libs.openapi.generator.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("springService") {
            id = "gr.odyssey.spring-service"
            implementationClass = "gr.odyssey.buildlogic.SpringServiceConventionPlugin"
        }
        create("openApiCodegen") {
            id = "gr.odyssey.openapi-codegen"
            implementationClass = "gr.odyssey.buildlogic.OpenApiCodegenConventionPlugin"
        }
    }
}
```

`libs.versions.kotlin.get()` works directly inside the `plugins {}` block — catalog
version accessors are resolved before the block runs, same mechanism that lets
`alias(libs.plugins.x)` work elsewhere. `java-gradle-plugin` auto-applies `java-library`
and adds `gradleApi()` to `compileOnlyApi`; combined with `maven-publish`, it also
auto-generates the two **plugin marker artifacts** required by the `plugins { id(...) }`
DSL — no extra configuration needed for that part.

`gradleKotlinDsl()` is a separate, explicit addition — `java-gradle-plugin` alone only
provides `gradleApi()` (the plain Java-facing Gradle API). The convention plugin classes
below use `configure<T> { }`/`withType<T>()` — Kotlin-only extension functions that ship in
`gradleKotlinDsl()`, not `gradleApi()`. This project doesn't apply the `kotlin-dsl` plugin
(that plugin is for *authoring scripts*, i.e. precompiled script plugins — the form Task 3
deliberately avoids per the user's decision above), so this dependency has to be added by
hand instead of coming along for free.

`gradlePluginPortal()` is required in `repositories {}` here: `ktlint-gradle-plugin` and
`openapi-generator-gradle-plugin` are published there, not to Maven Central — same as
`buildSrc`'s repositories block before this stage.

### The two convention plugins, as binary classes

Binary plugins can't use the `plugins {}` block's type-safe sugar (`kotlin("jvm")`,
`kotlin("plugin.spring")`) — that sugar is script-only. Apply by string ID via
`pluginManager.apply(...)` instead, and configure extensions via
`extensions.configure<T> { }` instead of the script DSL's implicit extension blocks.

**On lambda syntax: not applying `kotlin-dsl` has one more consequence.** Gradle's
precompiled script plugins get Kotlin's `sam-with-receiver` compiler plugin bundled in for
free via `kotlin-dsl`, which lets lambdas passed to Java `Action<T>` parameters (like
`Test.() -> Unit` for `configureEach { useJUnitPlatform() }`) use an implicit receiver.
Without `kotlin-dsl` applied, that sugar isn't available — those lambdas need an explicit
parameter instead (`configureEach { test -> test.useJUnitPlatform() }`). This only affects
calls into Gradle's/Java's own `Action<T>`-typed APIs; genuine Kotlin extension functions
with a real `T.() -> Unit` receiver, like `tasks.register<T>(name) { ... }`, are unaffected
and keep the implicit-receiver form. The two classes below apply this distinction
consistently — `project.tasks.withType<Test>().configureEach { test -> ... }` and
`the<KotlinJvmProjectExtension>().sourceSets.named("main") { sourceSet -> ... }` use explicit
parameters; `tasks.register<GenerateTask>(name) { ... }` in `ApiGeneration.kt` does not need
them.

`demo/build-logic-plugin/src/main/kotlin/gr/odyssey/buildlogic/SpringServiceConventionPlugin.kt`
(new file):

```kotlin
package gr.odyssey.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class SpringServiceConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val javaVersion = JavaVersion.VERSION_25

        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.jvm")
            apply("org.jetbrains.kotlin.plugin.spring")
            apply("org.springframework.boot")
            apply("org.jlleitschuh.gradle.ktlint")
        }

        project.extensions.configure<JavaPluginExtension> {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        project.repositories.mavenCentral()

        project.extensions.configure<KotlinJvmProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(javaVersion.majorVersion))
                freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
            }
        }

        project.tasks.withType<Test>().configureEach { test ->
            test.useJUnitPlatform()
            test.testLogging { testLogging ->
                testLogging.events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                testLogging.exceptionFormat = TestExceptionFormat.FULL
            }
        }
    }
}
```

`demo/build-logic-plugin/src/main/kotlin/gr/odyssey/buildlogic/OpenApiCodegenConventionPlugin.kt`
(new file):

```kotlin
package gr.odyssey.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class OpenApiCodegenConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val javaVersion = JavaVersion.VERSION_25

        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.jvm")
            apply("org.openapi.generator")
        }

        project.extensions.configure<JavaPluginExtension> {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        project.repositories.mavenCentral()

        project.extensions.configure<KotlinJvmProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(javaVersion.majorVersion))
                freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
            }
        }

        project.configureApiGeneration()
    }
}
```

`demo/build-logic-plugin/src/main/kotlin/gr/odyssey/buildlogic/ApiGeneration.kt` (new file)
— carried over from `buildSrc/src/main/kotlin/ApiGeneration.kt`, with the package
declaration added and the same explicit-SAM-parameter fix applied to its last two blocks:

```kotlin
package gr.odyssey.buildlogic

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

private const val TASK_GROUP = "openapi tools"

fun Project.configureApiGeneration(
    specPath: String = "openapi/odyssey-api.yaml",
    serverOutputDir: String = "generated/api/server",
    clientOutputDir: String = "generated/api/client",
    generateServer: Boolean = true,
    generateClient: Boolean = true,
) {
    val odysseySpec =
        layout.projectDirectory
            .file("src/main/resources/$specPath")
            .asFile.path
    val serverDir =
        layout.buildDirectory
            .dir(serverOutputDir)
            .get()
            .asFile.path
    val clientDir =
        layout.buildDirectory
            .dir(clientOutputDir)
            .get()
            .asFile.path
    val generatedSourceDirs = mutableListOf<String>()
    val codegenTasks = mutableListOf<TaskProvider<GenerateTask>>()

    if (generateServer) {
        val generateServerApi =
            tasks.register<GenerateTask>("generateServerApi") {
                description = "Generates servers for the Odyssey API"
                group = TASK_GROUP
                generatorName.set("kotlin-spring")
                inputSpec.set(odysseySpec)
                outputDir.set(serverDir)
                apiPackage.set("gr.odyssey.api.server")
                modelPackage.set("gr.odyssey.api.server.model")
                configOptions.set(
                    mapOf(
                        "interfaceOnly" to "true",
                        "useSpringBoot4" to "true",
                        "useJackson3" to "true",
                        "useSwaggerUI" to "true",
                        "suspendFunctions" to "true",
                        "reactive" to "true",
                    ),
                )
            }
        generatedSourceDirs += "$serverDir/src/main/kotlin"
        codegenTasks += generateServerApi
    }

    if (generateClient) {
        val generateClientApi =
            tasks.register<GenerateTask>("generateClientApi") {
                description = "Generates clients for the Odyssey API"
                group = TASK_GROUP
                generatorName.set("kotlin-spring")
                inputSpec.set(odysseySpec)
                outputDir.set(clientDir)
                apiPackage.set("gr.odyssey.api.client")
                modelPackage.set("gr.odyssey.api.client.model")
                configOptions.set(
                    mapOf(
                        "library" to "spring-declarative-http-interface",
                        "declarativeInterfaceReactiveMode" to "coroutines",
                        "useSpringBoot4" to "true",
                        "useJackson3" to "true",
                        "suspendFunctions" to "true",
                        "reactive" to "true",
                        "useFlowForArrayReturnType" to "false",
                    ),
                )
            }
        generatedSourceDirs += "$clientDir/src/main/kotlin"
        codegenTasks += generateClientApi
    }

    the<KotlinJvmProjectExtension>().sourceSets.named("main") { sourceSet ->
        sourceSet.kotlin.srcDirs(generatedSourceDirs)
    }

    tasks.named("compileKotlin") { task ->
        task.dependsOn(codegenTasks)
    }
}
```

Verify: `./gradlew compileKotlin` → `BUILD SUCCESSFUL`.

---

## Task 4 — Publish `build-logic-plugin` to `mavenLocal()`

```bash
./gradlew publishToMavenLocal
ls ~/.m2/repository/gr/odyssey/build-logic-plugin/1.0.0/
ls ~/.m2/repository/gr/odyssey/spring-service/gr.odyssey.spring-service.gradle.plugin/1.0.0/
ls ~/.m2/repository/gr/odyssey/openapi-codegen/gr.odyssey.openapi-codegen.gradle.plugin/1.0.0/
```

Verify: the first listing shows the actual plugin jar (`build-logic-plugin-1.0.0.jar`); the
other two show the **plugin marker artifacts** — tiny POM-only artifacts whose sole job is
to point `plugins { id(...) }` lookups at the real jar above. Both are generated
automatically by the `java-gradle-plugin` + `maven-publish` combination from Task 3 — no
separate task or configuration needed to produce them.

---

## Task 5 — Delete `buildSrc`; relocate the catalog file; rewire `odyssey-app`'s plugin and
catalog resolution

```bash
cd ../odyssey-app
rm -rf buildSrc
git mv gradle/libs.versions.toml libs.versions.toml
```

The relocation isn't cosmetic — see the third departure at the top of this card. Gradle
auto-detects `gradle/libs.versions.toml` at its conventional path as the default `libs`
catalog; that auto-detected catalog collides with the explicit `versionCatalogs { from(...) }`
declaration below ("Multiple 'from' invocations… you can only call the 'from' method a
single time"). Moving the file out of `gradle/` removes the conflicting candidate.

After moving the file, go back and update `odyssey-catalog/build.gradle.kts` (Task 2) to
read from the new path, then republish:

```kotlin
catalog {
    versionCatalog {
        from(files("../odyssey-app/libs.versions.toml"))
    }
}
```

```bash
cd ../odyssey-catalog
./gradlew publishToMavenLocal
cd ../odyssey-app
```

Rewrite `demo/odyssey-app/settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from("gr.odyssey:odyssey-catalog:1.0.0")
        }
    }
}

rootProject.name = "odyssey-app"

include("odyssey-api", "heroes-service", "monsters-service", "encounters-service")
```

Two separate additions doing two separate jobs: `pluginManagement.repositories` is where
Gradle looks up `plugins { id(...) }` requests (needs `mavenLocal()` to find
`gr.odyssey.spring-service`/`gr.odyssey.openapi-codegen`'s marker artifacts from Task 4).
`dependencyResolutionManagement.versionCatalogs` is where the `libs` catalog itself now
comes from — a published Maven coordinate instead of the local
`from(files("gradle/libs.versions.toml"))` Stage 4 would have used (that convention-based
auto-detection is what `odyssey-app` relied on implicitly before this stage, since no
`dependencyResolutionManagement` block existed at all until now).

`odyssey-app` no longer reads the catalog file directly at all — only `odyssey-catalog`
does (Task 2), so the relocated file remains the one real source text for every version in
the project, just resolved indirectly through the published artifact.

---

## Task 6 — Switch all four modules to the published plugin

Replace the `plugins {}` block in each of the four modules.

`demo/odyssey-api/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.odyssey.openapi.codegen)
}
```

`demo/heroes-service/build.gradle.kts`, `demo/monsters-service/build.gradle.kts`,
`demo/encounters-service/build.gradle.kts` (identical in all three):

```kotlin
plugins {
    alias(libs.plugins.odyssey.spring.service)
}
```

Nothing else in any of the four `build.gradle.kts` files changes — the `dependencies {}`
blocks from Stage 4 (bundles, `enforcedPlatform(libs.spring.boot.dependencies)`) are
untouched; only the plugin application line moves from a `buildSrc`-local
`id("gr.odyssey....")` to a catalog-resolved `alias(libs.plugins....)` pointing at the
now-published plugin. `odyssey-api`'s `build.gradle.kts` also drops its standalone
`java {}`, `configureApiGeneration()`, and `repositories {}` blocks — the convention plugin
now applies all three internally, so keeping them locally would duplicate configuration the
plugin already owns.

Verify:

```bash
./gradlew test   # BUILD SUCCESSFUL across all four modules
```

If this is the first time resolving the plugin, Gradle will print a one-time "Dependency
resolution for pluginManagement repositories" step hitting `mavenLocal()` — expected, not an
error.

---

## Task 7 — The payoff, shown live

Open `demo/build-logic-plugin` in its own window (a genuinely separate project directory —
no `include()` relationship to `odyssey-app` at all) side by side with
`demo/odyssey-app/heroes-service/build.gradle.kts`. Point out: `heroes-service` has zero
local copy of the Spring Boot/Kotlin convention, zero local copy of the catalog, and zero
Gradle-level relationship to `build-logic-plugin`'s source — only a plugin ID, an alias, and
a version number resolved from `mavenLocal()`. Do the same comparison for
`demo/odyssey-catalog` and `libs.versions.toml`'s `[plugins]` entries.

**Payoff moment:** make a real change inside `build-logic-plugin` — e.g. add a new compiler
flag to `SpringServiceConventionPlugin`'s `freeCompilerArgs` — bump its own `version` to
`"1.0.1"` in `build-logic-plugin/build.gradle.kts`, then:

```bash
./gradlew publishToMavenLocal
```

Back in `odyssey-app/libs.versions.toml`, bump the one line that drives the plugin version
(`buildLogicPlugin = "1.0.1"`), then republish `odyssey-catalog` — `odyssey-app` reads
`libs` from the **published** `odyssey-catalog:1.0.0` artifact (Task 5), not the TOML file
directly any more, so editing the file alone changes nothing until it's republished:

```toml
buildLogicPlugin = "1.0.1"
```

```bash
cd ../odyssey-catalog
./gradlew publishToMavenLocal
cd ../odyssey-app
./gradlew test --refresh-dependencies
```

`--refresh-dependencies` is required here specifically because `odyssey-catalog` republished
under the *same* coordinate (`1.0.0` — only its contents changed); Gradle caches resolved
`mavenLocal()` artifacts by coordinate and won't notice the new TOML content otherwise. This
is a one-off, demo-specific wrinkle, not something to dwell on live — a real release would
bump `odyssey-catalog`'s own version too, at which point this flag wouldn't be needed.

Pick a compiler flag that's actually valid for the installed Kotlin version before going
live — `-Xcontext-receivers` is rejected as deprecated under Kotlin 2.4 (`CONTEXT_PARAMETERS_ARE_DEPRECATED`).
Any no-op-safe flag works for the demo; the point is the propagation mechanism; rehearse the
exact flag beforehand.

All three services and `odyssey-api` pick up the change with **zero edits to any of their
four `build.gradle.kts` files** — only the catalog's single version line moved. This is the
full arc of the talk in one demo: a change to shared build logic, published once, consumed
everywhere, with nothing copied and nothing to keep in sync by hand.

Revert all three changes afterward — `build-logic-plugin`'s version and compiler arg,
`libs.versions.toml`'s `buildLogicPlugin` line — then republish both `odyssey-catalog` and
`build-logic-plugin` once more so `mavenLocal()` matches the committed `1.0.0` state.

Transition line (ties to the rehearsal script): "no copying, no drift — this is build logic
any project can adopt."

---

## Notes on scope, consistent with the design note's own "out of scope" section

- No real Maven/Nexus/Artifactory repository — `mavenLocal()` is explicitly sufficient per
  the design note ("sufficient for a live demo — no real repository needed").
  `~/.m2/repository` is local machine state; if rehearsing on a different machine, both
  `publishToMavenLocal` steps (Tasks 2 and 4) must run there first.
- `build-logic-plugin` and `odyssey-catalog` have no tests of their own in this stage —
  matches `buildSrc`'s own test-free history across Stages 3–4.
- No CI/release-pipeline ceremony for versioning the plugin or catalog — the Pros & Cons
  slide's con ("adds publishing and versioning ceremony... requires release discipline") is
  the point being illustrated, not something this demo needs to solve.