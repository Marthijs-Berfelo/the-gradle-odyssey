plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
    id("org.openapi.generator")
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}
