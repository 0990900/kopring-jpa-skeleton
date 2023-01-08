rootProject.name = "kopring-skeleton"
include(":module-core", ":module-jpa-core", ":module-jpa-api")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}
