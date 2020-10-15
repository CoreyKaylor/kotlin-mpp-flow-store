pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:3.5.4")
            }
        }
    }
}
rootProject.name = "MultiplatformPlay"


include(":shared")
include(":androidApp")

