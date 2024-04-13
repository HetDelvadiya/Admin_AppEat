

buildscript {
    repositories {
        mavenCentral()
        google()
        mavenCentral()
    }
    dependencies {
       classpath("com.android.tools.build:gradle:3.4.3")
        classpath("com.google.gms:google-services:4.4.1")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}


