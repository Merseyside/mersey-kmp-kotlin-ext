buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    `nexus-config`
}

allprojects {
    group = "io.github.merseyside"
    version = "1.2.6"
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}