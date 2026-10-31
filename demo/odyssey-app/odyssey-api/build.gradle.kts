plugins {
    alias(libs.plugins.odyssey.openapi.codegen)
}

group = "gr.odyssey"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
    implementation(libs.bundles.api)
}
