package skeleton

import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

fun Project.setupKtlint() {
    plugins.apply("org.jlleitschuh.gradle.ktlint")

    configure<KtlintExtension> {
        verbose.set(true)
        outputToConsole.set(true)
        coloredOutput.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}
