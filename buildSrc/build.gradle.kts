plugins {
    `kotlin-dsl`
}

dependencies {
    with(catalogGradle) {
        implementation(kotlin.gradle)
        implementation(android.gradle)
        implementation(moko.mobileMultiplatform)
        implementation(kotlin.serialization)
        //implementation(nexusPublish)
        implementation(kotlin.dokka)
        implementation(mersey.gradlePlugins)
        implementation(maven.publish.plugin)
        implementation("com.vanniktech:nexus:0.25.3")
    }
}