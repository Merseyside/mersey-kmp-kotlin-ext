@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.multiplatform)
        plugin(moko.multiplatform)
        plugin(kotlin.serialization)
        id(mersey.android.extension.id())
        id(mersey.kotlin.extension.id())
        plugin(kotlin.kapt)
    }
    `javadoc-stub-convention`
    `maven-publish-config`
}

android {
    namespace = "com.merseyside.merseyLib.kotlin"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
    }

    buildFeatures {
        dataBinding = true
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios()
    // Add the ARM64 simulator target
    iosSimulatorArm64()

    sourceSets {
        val iosMain by getting
        val iosSimulatorArm64Main by getting
        iosSimulatorArm64Main.dependsOn(iosMain)
    }
}

kotlinExtension {
    compilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
}

val libs = listOf(
    common.kotlin.stdlib,
    common.serialization,
    common.reflect
)

dependencies {
    libs.forEach { lib -> commonMainImplementation(lib) }
    commonMainImplementation(multiplatformLibs.coroutines)


}