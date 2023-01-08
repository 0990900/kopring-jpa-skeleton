@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlin.kapt.pluginId)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    api(project(":module-core"))
    api(libs.bundles.spring.boot.data)
    api(libs.querydsl.jpa)
    kapt(variantOf(libs.querydsl.apt) { classifier("jpa") })
    api(libs.h2)
    api(libs.mysql.connector)
}

tasks {
    noArg {
        annotation("javax.persistence.Entity")
    }
    allOpen {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.Embeddable")
        annotation("javax.persistence.MappedSuperclass")
    }
}
