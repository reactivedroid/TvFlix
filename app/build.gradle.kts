plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.compose")
    id("com.google.devtools.ksp")
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
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.android.tvflix"
        minSdk = Deps.Versions.min_sdk
        targetSdk = Deps.Versions.target_sdk
        versionCode = Deps.Versions.app_version_code
        versionName = Deps.Versions.app_version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        animationsDisabled = true
    }

    testBuildType = "debug"

    packaging {
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
        jvmTarget = "17"
    }
    namespace = "com.android.tvflix"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.Google.material)
    // start-region AndroidX
    implementation(Deps.AndroidX.ktx_core)
    implementation(Deps.AndroidX.ktx_fragment)
    implementation(Deps.AndroidX.ktx_activity)
    implementation(Deps.AndroidX.compose_activity)
    implementation(Deps.AndroidX.constraint_layout)
    implementation(platform(Deps.AndroidX.Compose.bom))
    implementation(Deps.AndroidX.Compose.ui)
    implementation(Deps.AndroidX.Compose.foundation)
    implementation(Deps.AndroidX.Compose.material)
    implementation(Deps.AndroidX.Compose.material_icons)
    implementation(Deps.AndroidX.Compose.tooling_preview)
    implementation(Deps.AndroidX.Compose.navigation)
    implementation(Deps.AndroidX.Compose.hilt_navigation)
    implementation(Deps.AndroidX.Lifecycle.viewmodel)
    implementation(Deps.AndroidX.Paging.runtime)
    implementation(Deps.AndroidX.Paging.compose)
    testImplementation(Deps.AndroidX.Paging.common)
    implementation(Deps.AndroidX.Room.runtime)
    ksp(Deps.AndroidX.Room.compiler)
    testImplementation(Deps.AndroidX.Room.testing)
    implementation(Deps.AndroidX.Room.ktx)
    implementation(Deps.AndroidX.annotation)
    // end-region AndroidX

    implementation(platform(Deps.OkHttp.okhttp_bom))
    implementation(Deps.OkHttp.main)
    implementation(Deps.OkHttp.logging_interceptor)
    implementation(Deps.Glide.runtime)
    implementation(Deps.Glide.okhttp_integration)
    implementation(Deps.Coil.compose)
    ksp(Deps.Glide.ksp)
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
    androidTestImplementation(Deps.AndroidX.Compose.test_junit4)
    testImplementation(Deps.Test.truth)
    testImplementation(Deps.Test.robolectric)
    testImplementation(Deps.Coroutines.test)
    // end-region Test

    implementation(Deps.Moshi.kotlin)
    ksp(Deps.Moshi.codegen)

    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    debugImplementation(Deps.Chucker.debug)
    debugImplementation(Deps.AndroidX.Compose.tooling)
    debugImplementation(Deps.AndroidX.Compose.test_manifest)
    releaseImplementation(Deps.Chucker.release)

    implementation(Deps.Hilt.android)
    ksp(Deps.Hilt.android_compiler)

    // start-region Firebase
    implementation(platform(Deps.Firebase.firebase_bom))
    implementation(Deps.Firebase.crashlytics)
    implementation(Deps.Firebase.performance_monitoring)
    implementation(Deps.Firebase.analytics)
    implementation(Deps.Firebase.remote_config)
    // end-region Firebase
}
