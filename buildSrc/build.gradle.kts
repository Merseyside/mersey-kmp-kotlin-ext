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
val gradle = "4.2.1"
val nexus = "1.1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("io.github.gradle-nexus:publish-plugin:$nexus")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$kotlin")
}