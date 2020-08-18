// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath(Deps.android_plugin)
        classpath(Deps.Kotlin.gradle_plugin)
        classpath(Deps.Hilt.gradlePlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
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
