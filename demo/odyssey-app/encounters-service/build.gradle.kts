plugins {
    id("gr.odyssey.spring-service")
}

group = "gr.odyssey"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":odyssey-api"))
    implementation(enforcedPlatform(libs.spring.boot.dependencies))
    implementation(libs.bundles.common)
    implementation(libs.bundles.api)
    implementation(libs.bundles.client)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.client.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
