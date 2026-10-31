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
