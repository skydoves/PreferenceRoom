# Retain generated class which implement PreferenceRoomImpl.
-keep public class ** implements com.skydoves.preferenceroom.PreferenceRoomImpl

# Prevent obfuscation of types which use PreferenceRoom annotations since the simple name
# is used to reflectively look up the generated Injector.
-keep class com.skydoves.preferenceroom.*
-keepclasseswithmembernames class * { @com.skydoves.preferenceroom.* <methods>; }
-keepclasseswithmembernames class * { @com.skydoves.preferenceroom.* <fields>; }