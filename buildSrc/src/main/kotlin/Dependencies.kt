object Deps {
    object Versions {
        const val compile_sdk = 28
        const val min_sdk = 21
        const val target_sdk = 26
        const val app_version_code = 104
        const val app_version_name = "2.1.0"
        const val android_plugin = "4.0.1"
        const val constraint_layout = "2.0.0-beta4"
        const val lifecycle = "2.2.0"
        const val android_test = "1.2.0"
        const val espresso = "3.2.0"
        const val glide = "4.11.0"
        const val junit = "4.12"
        const val mockito = "3.2.4"
        const val okhttp = "4.8.0"
        const val retrofit = "2.9.0"
        const val paging = "2.1.0"
        const val room = "2.2.5"
        const val kotlin = "1.3.72"
        const val timber = "4.7.1"
        const val mockito_kotlin = "2.2.0"
        const val arch_core_testing = "2.0.0"
        const val robolectric = "4.3.1"
        const val moshi = "1.9.2"
        const val coroutines = "1.3.7"
        const val truth = "1.0.1"
        const val annotation = "1.1.0"
        const val chucker = "3.2.0"
        const val ktx_core = "1.2.0"
        const val ktx_fragment = "1.2.4"
        const val ktx_activity = "1.1.0"
        const val hilt_dagger = "2.28-alpha"
        const val hilt = "1.0.0-alpha01"
        const val material = "1.1.0"
        const val android_test_junit = "1.1.1"
        const val multidex = "2.0.1"
    }

    const val android_plugin = "com.android.tools.build:gradle:${Versions.android_plugin}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val junit = "junit:junit:${Versions.junit}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    object Google {
        const val material = "com.google.android.material:material:${Versions.material}"
    }

    object Kotlin {
        const val gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
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
        }

        const val constraint_layout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

        const val ktx_core = "androidx.core:core-ktx:${Versions.ktx_core}"
        const val ktx_fragment = "androidx.fragment:fragment-ktx:${Versions.ktx_fragment}"
        const val ktx_activity = "androidx.activity:activity-ktx:${Versions.ktx_activity}"
        const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
        const val annotation = "androidx.annotation:annotation:${Versions.annotation}"

        object Lifecycle {
            const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
            const val compiler = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
        }

        object Room {
            const val runtime = "androidx.room:room-runtime:${Versions.room}"
            const val compiler = "androidx.room:room-compiler:${Versions.room}"
            const val ktx = "androidx.room:room-ktx:${Versions.room}"
            const val testing = "androidx.room:room-testing:${Versions.room}"
        }

        object Hilt {
            const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt}"
            const val compiler = "androidx.hilt:hilt-compiler:${Versions.hilt}"
        }
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt_dagger}"
        const val android_compiler =
            "com.google.dagger:hilt-android-compiler:${Versions.hilt_dagger}"
        const val gradlePlugin =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_dagger}"
    }

    object Retrofit {
        const val main = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    }

    object OkHttp {
        const val main = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        const val logging_interceptor =
            "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    }

    object Glide {
        const val runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
        const val okhttp_integration =
            "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    }

    object Moshi {
        const val kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    }

    object Mockito {
        const val core = "org.mockito:mockito-core:${Versions.mockito}"
        const val android = "org.mockito:mockito-android:${Versions.mockito}"
        const val inline = "org.mockito:mockito-inline:${Versions.mockito}"
        const val kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito_kotlin}"
    }

    object Chucker {
        const val debug = "com.github.chuckerteam.chucker:library:${Versions.chucker}"
        const val release = "com.github.chuckerteam.chucker:library-no-op:${Versions.chucker}"
    }

    const val truth = "com.google.truth:truth:${Versions.truth}"
}
