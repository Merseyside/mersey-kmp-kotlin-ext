plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
}

val kotlin = "1.6.0"
val gradle = "7.0.3"
val nexus = "1.1.0"
val multiplatform = "0.12.0"

dependencies {
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("dev.icerock:mobile-multiplatform:$multiplatform")
    implementation("io.github.gradle-nexus:publish-plugin:$nexus")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$kotlin")
}