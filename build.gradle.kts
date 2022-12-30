import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import skeleton.setupDetekt

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    id(libs.plugins.kotlin.jvm.pluginId) apply false
    id(libs.plugins.kotlin.kapt.pluginId) apply false
    id(libs.plugins.detekt.pluginId) apply false
    alias(libs.plugins.springframework.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.kover) apply false
}

val kopringProjects = listOf(project(":module-core"), project(":module-api"))

configure(kopringProjects) {
    group = "kopring"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = rootProject.libs.plugins.kotlin.jvm.pluginId)
    apply(plugin = rootProject.libs.plugins.detekt.pluginId)
    apply(plugin = rootProject.libs.plugins.springframework.boot.pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.spring.pluginId)
    apply(plugin = rootProject.libs.plugins.dokka.pluginId)
    apply(plugin = rootProject.libs.plugins.kover.pluginId)

    @Suppress("UnstableApiUsage")
    dependencies {
        val api by configurations
        val developmentOnly by configurations
        api(rootProject.libs.arrow.core)
        api(rootProject.libs.bundles.kotlin)
        api(rootProject.libs.logback.classic)
        developmentOnly(rootProject.libs.spring.boot.devtools)
        testFixtures(rootProject.libs.spring.boot.starter.test)
    }

    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
        }

        withType<Test> {
            useJUnitPlatform()
            extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
                includes = listOf("skeleton.*")
            }
        }
    }

    extra.set("dokka.outputDirectory", rootDir.resolve("docs"))
    setupDetekt()
}

allprojects {
    repositories {
        mavenCentral()
    }
}
