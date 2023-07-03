dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    val catalogVersions = "1.7.4"
    val group = "io.github.merseyside"
    versionCatalogs {

        val catalogGradle by creating {
            from("$group:catalog-version-gradle:$catalogVersions")
            version("mokoMobileMultiplatform", "0.14.1")
            version("maven-publish-plugin", "0.25.3")
        }
    }
}