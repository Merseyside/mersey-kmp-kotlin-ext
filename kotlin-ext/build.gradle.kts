plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
    id(Plugins.kotlinSerialization)
    `maven-publish-config`
}

val libs = listOf(
    common.kotlin.stdlib,
    common.serialization,
    common.coroutines,
    common.reflect
)

dependencies {
    libs.forEach { lib -> implementation(lib) }
}

java {
    withSourcesJar()
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