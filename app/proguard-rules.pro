# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html


#To fix: Warning: com.comscore.instrumentation.InstrumentedMapActivity: can't find referenced class com.google.android.maps.MapActivity
-dontwarn  com.comscore.instrumentation.InstrumentedMapActivity

# It's safe to ignore okio and okhttp warnings
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

# ProGuard configuration for production build only
# Remove Log.* method calls
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
-dontwarn org.apache.**

# Design support library
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

# FOR APPCOMPAT 23.1.1 and higher to avoid crashes on specific devices
# Need to check if it will be necessary starting with 24.0.0
# https://code.google.com/p/android/issues/detail?id=78377#c336
-keep class !android.support.v7.view.menu.*MenuBuilder*, android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }


# Needed by commons logging
-keep class org.apache.commons.logging.* { *; }

#Rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

#Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
#Retrolambda
-dontwarn java.lang.invoke.*

#Dont obfuscate response classes used in Gson parsing
-keep class com.android.ashwiask.tvmaze.AirChannel { *; }
-keep class com.android.ashwiask.tvmaze.Episode { *; }
-keep class com.android.ashwiask.tvmaze.ExternalInfo { *; }
-keep class com.android.ashwiask.tvmaze.Show { *; }

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
        public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
        @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
        public static final ** CREATOR;
}

-keep class android.databinding.** { *; }
-dontwarn android.databinding.**

# dagger
-dontwarn com.google.errorprone.annotations.*
# Lambda
-dontwarn **$$Lambda$*

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.GeneratedAppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
-dontwarn com.bumptech.glide.integration.okhttp3.*