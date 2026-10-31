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

