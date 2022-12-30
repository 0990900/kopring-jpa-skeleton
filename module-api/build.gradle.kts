@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlin.kapt.pluginId)
}

dependencies {
    api(project(":module-core"))
    api(libs.bundles.spring.boot.web)
    api(libs.prometheus)
    api(libs.bundles.spring.doc)
    api(libs.bundles.json.web.token)
    api(libs.caffeine)
    kapt(libs.spring.boot.configuration.processor)
}

tasks {
    bootJar {
        enabled = false
    }
}
