allprojects {
    repositories {
        mavenLocal()

        google()
        jcenter()

        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://dl.bintray.com/aakira/maven") }
        maven { url = uri("https://jetbrains.bintray.com/kotlin-native-dependencies") }
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}