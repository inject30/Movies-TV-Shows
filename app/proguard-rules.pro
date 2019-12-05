# Add project specific ProGuard rules here.
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-keep class com.google.android.gms.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep class android.support.v7.* { *; }
-keep interface android.support.v7.* { *; }
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.* { *; }

#-dontwarn com.google.android.gms.**
-dontwarn android.support.v7.**
-dontwarn com.squareup.**
-dontwarn com.squareup.okhttp.**

# For Google Play Services
-keep public class com.google.android.gms.ads.**{
   public *;
}

# For old ads classes
-keep public class com.google.ads.**{
   public *;
}

# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

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

-keepattributes EnclosingMethod
-keepattributes InnerClasses

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable