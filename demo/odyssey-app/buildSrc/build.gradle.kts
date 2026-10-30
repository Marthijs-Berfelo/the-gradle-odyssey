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
