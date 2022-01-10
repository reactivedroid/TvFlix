plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.firebase.appdistribution")
}

apply {
    from(rootProject.file("tools/checkstyle.gradle"))
    from(rootProject.file("tools/pmd.gradle"))
}

android {
    compileSdk = Deps.Versions.compile_sdk

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.android.tvflix"
        minSdk = Deps.Versions.min_sdk
        targetSdk = Deps.Versions.target_sdk
        versionCode = Deps.Versions.app_version_code
        versionName = Deps.Versions.app_version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                // Refer https://developer.android.com/jetpack/androidx/releases/room#compiler-options
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }
    flavorDimensions.addAll(listOf("default"))
    productFlavors {
        create("prod") {
            dimension = "default"
            applicationId = "com.android.tvflix"
            firebaseAppDistribution {
                releaseNotesFile = "release-notes.txt"
                groupsFile = "tester-groups.txt"
                serviceCredentialsFile = "tvflix-b45cd-5fa9e2fb3108.json"
            }
        }
        create("dev") {
            applicationId = "com.android.tvflix"
            firebaseAppDistribution {
                releaseNotesFile = "release-notes.txt"
                groupsFile = "tester-groups.txt"
                serviceCredentialsFile = "tvflix-b45cd-5fa9e2fb3108.json"
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        animationsDisabled = true
    }

    kapt {
        useBuildCache = true
        javacOptions {
            // Increase the max count of errors from annotation processors.
            // Default is 100.
            option("-Xmaxerrs", 500)
        }
    }
    testBuildType = "debug"

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/ASL2.0",
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt",
                ".readme"
            )
        )
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.Google.material)
    // start-region AndroidX
    implementation(Deps.AndroidX.ktx_core)
    implementation(Deps.AndroidX.ktx_fragment)
    implementation(Deps.AndroidX.ktx_activity)
    implementation(Deps.AndroidX.constraint_layout)
    kapt(Deps.AndroidX.Lifecycle.compiler)
    implementation(Deps.AndroidX.Lifecycle.viewmodel)
    implementation(Deps.AndroidX.Paging.runtime)
    testImplementation(Deps.AndroidX.Paging.common)
    implementation(Deps.AndroidX.Room.runtime)
    kapt(Deps.AndroidX.Room.compiler)
    testImplementation(Deps.AndroidX.Room.testing)
    implementation(Deps.AndroidX.Room.ktx)
    implementation(Deps.AndroidX.annotation)
    // end-region AndroidX

    implementation(platform(Deps.OkHttp.okhttp_bom))
    implementation(Deps.OkHttp.main)
    implementation(Deps.OkHttp.logging_interceptor)
    implementation(Deps.Glide.runtime)
    implementation(Deps.Glide.okhttp_integration)
    kapt(Deps.Glide.compiler)
    implementation(Deps.Retrofit.main)
    implementation(Deps.Retrofit.moshi)

    implementation(Deps.timber)

    // start-region Test
    testImplementation(Deps.junit)
    testImplementation(Deps.Test.Mockito.core)
    androidTestImplementation(Deps.Test.Mockito.android)
    testImplementation(Deps.Test.Mockito.kotlin)
    testImplementation(Deps.Test.Mockito.inline)
    testImplementation(Deps.AndroidX.Test.arch_core_testing)
    testImplementation(Deps.AndroidX.Test.core)
    androidTestImplementation(Deps.AndroidX.Test.runner)
    androidTestImplementation(Deps.AndroidX.Test.junit)
    // Espresso
    androidTestImplementation(Deps.AndroidX.Test.Espresso.core)
    androidTestImplementation(Deps.AndroidX.Test.Espresso.contrib)
    androidTestImplementation(Deps.AndroidX.Test.Espresso.idling_resource)
    androidTestImplementation(Deps.AndroidX.Test.rules)
    testImplementation(Deps.Test.truth)
    testImplementation(Deps.Test.robolectric)
    testImplementation(Deps.Coroutines.test)
    // end-region Test

    implementation(Deps.Moshi.kotlin)
    kapt(Deps.Moshi.codegen)

    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    debugImplementation(Deps.Chucker.debug)
    releaseImplementation(Deps.Chucker.release)

    implementation(Deps.Hilt.android)
    kapt(Deps.Hilt.android_compiler)

    // start-region Firebase
    implementation(platform(Deps.Firebase.firebase_bom))
    implementation(Deps.Firebase.crashlytics)
    implementation(Deps.Firebase.performance_monitoring)
    implementation(Deps.Firebase.analytics)
    implementation(Deps.Firebase.remote_config)
    // end-region Firebase
}


