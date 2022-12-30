plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleKotlinDsl())
    implementation(libs.kotlin.gradle)
    implementation(libs.detekt.gradle)
}
