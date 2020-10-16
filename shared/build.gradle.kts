import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
}
group = "com.sample"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://kotlin.bintray.com/kotlinx")
    }
}
kotlin {
    android()
    iosX64("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9-native-mt-2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk7"))
                implementation("androidx.core:core-ktx:1.2.0")
            }
        }
        val androidTest by getting {}
        val iosMain by getting {}
        val iosTest by getting {}
    }
}
android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=org.mylibrary.OptInAnnotation"
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    kotlin.targets.withType<KotlinNativeTarget>().forEach {
        val framework = it.binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(buildDir, "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
}
tasks.getByName("build").dependsOn(packForXcode)