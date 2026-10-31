plugins {
    alias(libs.plugins.odyssey.spring.service)
}

group = "gr.odyssey"
version = "0.0.1-SNAPSHOT"

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
    testImplementation(libs.bundles.server.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
