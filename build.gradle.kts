// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.AppPlugins.android_gradle)
        classpath(Deps.AppPlugins.kotlin)
        classpath(Deps.AppPlugins.hilt)
        classpath(Deps.AppPlugins.gms)
        classpath(Deps.AppPlugins.Firebase.crashlytics)
        classpath(Deps.AppPlugins.Firebase.performance)
        classpath(Deps.AppPlugins.Firebase.app_distribution)
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    configurations.all {
        resolutionStrategy.eachDependency {
            when {
                requested.name.startsWith("kotlin-stdlib") -> {
                    useTarget(
                        "${requested.group}:${requested.name.replace(
                            "jre",
                            "jdk"
                        )}:${requested.version}"
                    )
                }
                else -> when (requested.group) {
                    "org.jetbrains.kotlin" -> useVersion(Deps.Versions.kotlin)
                }
            }
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}
