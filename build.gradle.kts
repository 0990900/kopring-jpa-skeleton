import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import skeleton.setupDetekt
import skeleton.setupKtlint

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    val plugins = libs.plugins
    application
    id(plugins.kotlin.jvm.pluginId) apply false
    id(plugins.kotlin.kapt.pluginId) apply false
    id(plugins.detekt.pluginId) apply false
    id(plugins.ktlint.pluginId) apply false
    alias(plugins.springframework.boot) apply false
    alias(plugins.spring.dependency.management) apply false
    alias(plugins.kotlin.spring) apply false
    alias(plugins.kotlin.jpa) apply false
    alias(plugins.dokka) apply false
    alias(plugins.kover) apply false
}

val kopringProjects = listOf(project(":module-core"), project(":module-api"))

configure(kopringProjects) {
    val libs = rootProject.libs
    val plugins = libs.plugins
    group = "kopring"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = plugins.kotlin.jvm.pluginId)
    apply(plugin = plugins.springframework.boot.pluginId)
    apply(plugin = plugins.spring.dependency.management.pluginId)
    apply(plugin = plugins.kotlin.spring.pluginId)
    apply(plugin = plugins.dokka.pluginId)
    apply(plugin = plugins.kover.pluginId)

    @Suppress("UnstableApiUsage")
    dependencies {
        val api by configurations
        val developmentOnly by configurations
        api(libs.arrow.core)
        api(libs.bundles.kotlin)
        api(libs.logback.classic)
        developmentOnly(libs.spring.boot.devtools)
        testFixtures(libs.spring.boot.starter.test)
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

        withType<DokkaTask> {
            outputDirectory.set(rootDir.resolve("docs"))
        }
    }

    setupDetekt()
    setupKtlint()
}

allprojects {
    repositories {
        mavenCentral()
    }
}
