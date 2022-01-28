plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinMultiplatformConvention)
    id(Plugins.mobileMultiplatform)
    id(Plugins.kotlinSerialization)
    id(Plugins.iosFramework)
    id(Plugins.mavenPublishConfig)
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }
}

val libs = listOf(
    common.serialization,
    common.coroutines,
    common.reflect
)

dependencies {
    libs.forEach { lib -> commonMainImplementation(lib) }
}