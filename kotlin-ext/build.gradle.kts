plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
    id(Plugins.kotlinSerialization)
    `maven-publish-config`
}

val libs = listOf(
    common.serialization
)

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")

    libs.forEach { lib -> implementation(lib) }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = Metadata.groupId
            version = Metadata.version
            artifactId = project.name

            from(components["java"])
        }
    }
}