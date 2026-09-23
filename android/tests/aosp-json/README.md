# Android JSON implementation for JVM tests

The six files in `org/json` are unmodified Apache 2.0 source from the Android Open Source Project, branch `android15-release`, retrieved on 2026-09-13:

[AOSP libcore JSON source](https://android.googlesource.com/platform/libcore/+/refs/heads/android15-release/json/src/main/java/org/json/)

Each file preserves its original copyright and license header. `android/annotation`, `android/compat/annotation`, and `libcore/util` contain test-only no-op annotation declarations so these Android sources compile on a stock JDK. These files are not packaged in the APK.

This lets the native save tests exercise Android's real JSON parser/serializer, including envelope escaping and malformed inputs, rather than replacing parsing with a test stub.
