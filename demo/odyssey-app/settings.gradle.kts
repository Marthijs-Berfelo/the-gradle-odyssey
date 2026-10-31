pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from("gr.odyssey:odyssey-catalog:1.0.0")
        }
    }
}

rootProject.name = "odyssey-app"

include("odyssey-api", "heroes-service", "monsters-service", "encounters-service")
