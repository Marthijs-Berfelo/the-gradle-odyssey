import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("jvm") version "2.3.21"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.25.0"
}

group = "gr.odyssey"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_25
}

configureApiGeneration()

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.1.1")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.54")
}

fun configureApiGeneration() {
    val taskGroup = "openapi tools"
    val odysseySpec =
        layout.projectDirectory
            .file("src/main/resources/openapi/odyssey-api.yaml")
            .asFile.path
    val generatedServerDir =
        layout.buildDirectory
            .dir("generated/api/server")
            .get()
            .asFile
            .path
    val generatedClientDir =
        layout.buildDirectory
            .dir("generated/api/client")
            .get()
            .asFile
            .path

    val generateServerApi =
        tasks.register<GenerateTask>("generateServerApi") {
            description = "Generates servers for the Odyssey API"
            group = taskGroup
            generatorName.set("kotlin-spring")
            inputSpec.set(odysseySpec)
            outputDir.set(generatedServerDir)
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

    val generateClientApi =
        tasks.register<GenerateTask>("generateClientApi") {
            description = "Generates client's for the Odyssey API"
            group = taskGroup
            generatorName.set("kotlin-spring")
            inputSpec.set(odysseySpec)
            outputDir.set(generatedClientDir)
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

    sourceSets {
        main {
            kotlin {
                srcDirs(
                    "$generatedClientDir/src/main/kotlin",
                    "$generatedServerDir/src/main/kotlin",
                )
            }
        }
    }

    tasks.named("compileKotlin") {
        dependsOn(generateServerApi, generateClientApi)
    }
}
