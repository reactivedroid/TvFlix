// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.AppPlugins.android_gradle)
        classpath(Deps.AppPlugins.Kotlin.gradle)
        classpath(Deps.AppPlugins.Kotlin.compose)
        classpath(Deps.AppPlugins.ksp)
        classpath(Deps.AppPlugins.hilt)
        classpath(Deps.AppPlugins.gms)
        classpath(Deps.AppPlugins.Firebase.crashlytics)
        classpath(Deps.AppPlugins.Firebase.performance)
        classpath(Deps.AppPlugins.Firebase.app_distribution)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean").configure {
    delete("build")
}
