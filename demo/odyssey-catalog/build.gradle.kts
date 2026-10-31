plugins {
    `version-catalog`
    `maven-publish`
}

group = "gr.odyssey"
version = "1.0.0"

catalog {
    versionCatalog {
        from(files("../odyssey-app/libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
}
