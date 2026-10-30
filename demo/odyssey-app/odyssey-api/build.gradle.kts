plugins {
    id("gr.odyssey.openapi-codegen")
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

dependencies {
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
    implementation(libs.bundles.api)
}
