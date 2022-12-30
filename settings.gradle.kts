rootProject.name = "kopring-jpa-skeleton"
include(":module-core", ":module-api")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}
