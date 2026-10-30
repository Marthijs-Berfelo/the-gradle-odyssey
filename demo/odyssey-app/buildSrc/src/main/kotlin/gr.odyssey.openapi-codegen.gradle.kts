import gradle.kotlin.dsl.accessors._bdbb04adcb1609ca7d3d5fc03c8b5f5b.java
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
