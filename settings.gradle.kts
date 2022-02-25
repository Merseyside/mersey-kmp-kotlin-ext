enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val catalogVersions = "1.3.0"
    val group = "io.github.merseyside"
    versionCatalogs {
        val common by creating {
            from("$group:catalog-version-common:$catalogVersions")
        }
    }
}

include(":kotlin-ext")

rootProject.name="kotlin-extensions"
rootProject.buildFileName = "build.gradle.kts"

