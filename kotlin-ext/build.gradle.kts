plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.multiplatform)
        plugin(kotlin.serialization)
        id(mersey.android.extension.id())
        id(mersey.kotlin.extension.id())
        plugin(kotlin.kapt)
    }
    `maven-publish-plugin`
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
    androidTarget {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    // Add apple targets
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()
}

kotlinExtension {
    compilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xexpect-actual-classes")
}

val libs = listOf(
    common.kotlin.stdlib,
    common.serialization,
    common.kotlin.reflect,
    multiplatformLibs.coroutines
)

dependencies {
    implementation(androidLibs.androidx.core)
    libs.forEach { lib -> commonMainImplementation(lib) }
}