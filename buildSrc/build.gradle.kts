plugins {
    `kotlin-dsl`
}

dependencies {
    with(catalogGradle) {
        implementation(kotlin.gradle)
        implementation(android.gradle)
        implementation(moko.mobileMultiplatform)
        implementation(kotlin.serialization)
        implementation(kotlin.dokka)
        implementation(mersey.gradlePlugins)
        implementation(maven.publish.plugin)
    }
}