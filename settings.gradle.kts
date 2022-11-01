enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()

        google()
    }

    val catalogVersions = "1.5.9"
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

rootProject.name="kmp-kotlin-extensions"
rootProject.buildFileName = "build.gradle.kts"

