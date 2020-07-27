plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("dagger.hilt.android.plugin")
}

apply {
    from(rootProject.file("tools/checkstyle.gradle"))
    from(rootProject.file("tools/pmd.gradle"))
}

android {
    compileSdkVersion(Deps.Versions.compile_sdk)

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.android.tvflix"
        minSdkVersion(Deps.Versions.min_sdk)
        targetSdkVersion(Deps.Versions.target_sdk)
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
    flavorDimensions("default")
    productFlavors {
        create("prod") {
            applicationId = "com.android.tvflix"
        }
        create("dev") {
            applicationId = "com.android.tvflix.dev"
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
        exclude("META-INF/ASL2.0")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
        exclude(".readme")
    }

    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(Deps.AndroidX.Lifecycle.extensions)
    kapt(Deps.AndroidX.Lifecycle.compiler)
    implementation(Deps.AndroidX.Lifecycle.viewmodel)
    implementation(Deps.AndroidX.Lifecycle.livedata)
    implementation(Deps.AndroidX.Paging.runtime)
    testImplementation(Deps.AndroidX.Paging.common)
    implementation(Deps.AndroidX.Room.runtime)
    kapt(Deps.AndroidX.Room.compiler)
    testImplementation(Deps.AndroidX.Room.testing)
    implementation(Deps.AndroidX.Room.ktx)
    implementation(Deps.AndroidX.Hilt.viewmodel)
    kapt(Deps.AndroidX.Hilt.compiler)
    implementation(Deps.AndroidX.multidex)
    implementation(Deps.AndroidX.annotation)
    // end-region AndroidX

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
    testImplementation(Deps.Mockito.core)
    androidTestImplementation(Deps.Mockito.android)
    testImplementation(Deps.Mockito.kotlin)
    testImplementation(Deps.Mockito.inline)
    testImplementation(Deps.robolectric)
    testImplementation(Deps.AndroidX.Test.arch_core_testing)
    testImplementation(Deps.AndroidX.Test.core)
    androidTestImplementation(Deps.AndroidX.Test.runner)
    androidTestImplementation(Deps.AndroidX.Test.junit)
    // Espresso
    androidTestImplementation(Deps.AndroidX.Test.Espresso.core)
    androidTestImplementation(Deps.AndroidX.Test.Espresso.contrib)
    androidTestImplementation(Deps.AndroidX.Test.Espresso.idling_resource)
    androidTestImplementation(Deps.AndroidX.Test.rules)
    testImplementation(Deps.truth)
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
}


