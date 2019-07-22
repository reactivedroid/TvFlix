// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.fabric.io/public")
        google()
        jcenter()
    }
    dependencies {
        classpath(Deps.android_plugin)
        classpath(Deps.kotlin_gradle_plugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        maven("https://maven.google.com")
        maven("http://repository.jetbrains.com/all")
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
