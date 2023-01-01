plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(gradleKotlinDsl())
    implementation(libs.kotlin.gradle)
    implementation(libs.detekt.gradle)
    implementation(libs.ktlint.gradle)
}
