// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // Thêm dòng này vào
    id("com.google.gms.google-services") version "4.4.2" apply false
}