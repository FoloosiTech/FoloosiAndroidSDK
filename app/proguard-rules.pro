-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.foloosi.**
-keep class com.foloosi.** {*;}

-optimizations !method/inlining/*