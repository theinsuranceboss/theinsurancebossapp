# Retrofit / Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.theinsuranceboss.app.data.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-dontwarn okhttp3.**
-dontwarn retrofit2.**
