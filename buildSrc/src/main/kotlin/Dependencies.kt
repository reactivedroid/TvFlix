object Deps {
    object Versions {
        const val compile_sdk = 35
        const val min_sdk = 23
        const val target_sdk = 35
        const val app_version_code = 200
        const val app_version_name = "3.0.0"
        const val android_plugin = "8.7.3"
        const val constraint_layout = "2.2.1"
        const val lifecycle = "2.8.7"
        const val android_test = "1.6.1"
        const val espresso = "3.6.1"
        const val glide = "4.16.0"
        const val junit = "4.13.2"
        const val mockito = "5.14.2"
        const val okhttp = "4.12.0"
        const val retrofit = "2.11.0"
        const val paging = "3.3.5"
        const val room = "2.6.1"
        const val kotlin = "2.1.0"
        const val ksp = "2.1.0-1.0.29"
        const val timber = "5.0.1"
        const val mockito_kotlin = "5.4.0"
        const val arch_core_testing = "2.2.0"
        const val moshi = "1.15.2"
        const val coroutines = "1.9.0"
        const val truth = "1.4.4"
        const val annotation = "1.9.1"
        const val chucker = "4.1.0"
        const val ktx_core = "1.15.0"
        const val ktx_fragment = "1.8.5"
        const val ktx_activity = "1.9.3"
        const val compose_bom = "2024.12.01"
        const val compose_activity = "1.9.3"
        const val compose_navigation = "2.8.5"
        const val compose_hilt_navigation = "1.2.0"
        const val paging_compose = "3.3.5"
        const val coil_compose = "2.7.0"
        const val hilt = "2.53.1"
        const val material = "1.12.0"
        const val android_test_junit = "1.2.1"
        const val robolectric = "4.14.1"
        const val firebase_bom = "33.7.0"
        const val firebase_crashlytics_plugin = "3.0.2"
        const val firebase_performance_plugin = "1.4.2"
        const val gms_version = "4.4.2"
        const val app_distribution = "5.0.0"
    }

    object AppPlugins {
        const val android_gradle = "com.android.tools.build:gradle:${Versions.android_plugin}"

        object Firebase {
            const val crashlytics =
                "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebase_crashlytics_plugin}"
            const val performance =
                "com.google.firebase:perf-plugin:${Versions.firebase_performance_plugin}"
            const val app_distribution =
                "com.google.firebase:firebase-appdistribution-gradle:${Versions.app_distribution}"
        }

        object Kotlin {
            const val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
            const val compose = "org.jetbrains.kotlin:compose-compiler-gradle-plugin:${Deps.Versions.kotlin}"
        }
        const val ksp = "com.google.devtools.ksp:symbol-processing-gradle-plugin:${Versions.ksp}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
        const val gms = "com.google.gms:google-services:${Versions.gms_version}"
    }

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val junit = "junit:junit:${Versions.junit}"

    object Google {
        const val material = "com.google.android.material:material:${Versions.material}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    }

    object AndroidX {
        object Test {
            const val core = "androidx.test:core:${Versions.android_test}"
            const val rules = "androidx.test:rules:${Versions.android_test}"
            const val runner = "androidx.test:runner:${Versions.android_test}"
            const val junit = "androidx.test.ext:junit:${Versions.android_test_junit}"

            object Espresso {
                const val core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
                const val contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
                const val idling_resource =
                    "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
            }

            const val arch_core_testing =
                "androidx.arch.core:core-testing:${Versions.arch_core_testing}"
        }

        object Paging {
            const val common = "androidx.paging:paging-common-ktx:${Versions.paging}"
            const val runtime = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
            const val compose = "androidx.paging:paging-compose:${Versions.paging_compose}"
        }

        const val constraint_layout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

        const val ktx_core = "androidx.core:core-ktx:${Versions.ktx_core}"
        const val ktx_fragment = "androidx.fragment:fragment-ktx:${Versions.ktx_fragment}"
        const val ktx_activity = "androidx.activity:activity-ktx:${Versions.ktx_activity}"
        const val compose_activity =
            "androidx.activity:activity-compose:${Versions.compose_activity}"
        const val annotation = "androidx.annotation:annotation:${Versions.annotation}"

        object Compose {
            const val bom = "androidx.compose:compose-bom:${Versions.compose_bom}"
            const val ui = "androidx.compose.ui:ui"
            const val foundation = "androidx.compose.foundation:foundation"
            const val material = "androidx.compose.material:material"
            const val material_icons = "androidx.compose.material:material-icons-core"
            const val tooling_preview = "androidx.compose.ui:ui-tooling-preview"
            const val tooling = "androidx.compose.ui:ui-tooling"
            const val test_junit4 = "androidx.compose.ui:ui-test-junit4"
            const val test_manifest = "androidx.compose.ui:ui-test-manifest"
            const val navigation =
                "androidx.navigation:navigation-compose:${Versions.compose_navigation}"
            const val hilt_navigation =
                "androidx.hilt:hilt-navigation-compose:${Versions.compose_hilt_navigation}"
        }

        object Lifecycle {
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
            const val compiler = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
        }

        object Room {
            const val runtime = "androidx.room:room-runtime:${Versions.room}"
            const val compiler = "androidx.room:room-compiler:${Versions.room}"
            const val ktx = "androidx.room:room-ktx:${Versions.room}"
            const val testing = "androidx.room:room-testing:${Versions.room}"
        }
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val android_compiler =
            "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    }

    object Retrofit {
        const val main = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    }

    object OkHttp {
        const val okhttp_bom = "com.squareup.okhttp3:okhttp-bom:${Versions.okhttp}"
        const val main = "com.squareup.okhttp3:okhttp"
        const val logging_interceptor =
            "com.squareup.okhttp3:logging-interceptor"
    }

    object Glide {
        const val runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val ksp = "com.github.bumptech.glide:ksp:${Versions.glide}"
        const val okhttp_integration =
            "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    }

    object Coil {
        const val compose = "io.coil-kt:coil-compose:${Versions.coil_compose}"
    }

    object Moshi {
        const val kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    }

    object Test {
        object Mockito {
            const val core = "org.mockito:mockito-core:${Versions.mockito}"
            const val android = "org.mockito:mockito-android:${Versions.mockito}"
            const val inline = "org.mockito:mockito-inline:${Versions.mockito}"
            const val kotlin =
                "org.mockito.kotlin:mockito-kotlin:${Versions.mockito_kotlin}"
        }

        const val truth = "com.google.truth:truth:${Versions.truth}"
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    }


    object Chucker {
        const val debug = "com.github.chuckerteam.chucker:library:${Versions.chucker}"
        const val release = "com.github.chuckerteam.chucker:library-no-op:${Versions.chucker}"
    }

    object Firebase {
        const val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebase_bom}"
        const val crashlytics = "com.google.firebase:firebase-crashlytics"
        const val performance_monitoring = "com.google.firebase:firebase-perf"
        const val analytics = "com.google.firebase:firebase-analytics"
        const val remote_config = "com.google.firebase:firebase-config"
    }
}
