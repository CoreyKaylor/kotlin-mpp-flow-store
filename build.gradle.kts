buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        maven {
            url = uri("https://kotlin.bintray.com/kotlinx")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:3.5.4")
    }
}
group = "com.sample"
version = "1.0-SNAPSHOT"