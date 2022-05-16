enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val catalogVersions = "1.4.7"
    val group = "io.github.merseyside"
    versionCatalogs {
        val common by creating {
            from("$group:catalog-version-common:$catalogVersions")
        }

        val multiplatformLibs by creating {
            from("$group:catalog-version-multiplatform:$catalogVersions")
        }

        val catalogPlugins by creating {
            from("$group:catalog-version-plugins:$catalogVersions")
        }
    }
}

include(":kotlin-ext")

rootProject.name="kotlin-extensions"
rootProject.buildFileName = "build.gradle.kts"

