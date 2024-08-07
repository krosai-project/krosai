rootProject.name = "krosai"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        mavenCentral()
    }
}

include(":krosai-core")
include(":krosai-sample")

rootProject.projectDir.resolve("models").list()?.forEach {
    include("models:$it")
}

rootProject.projectDir.resolve("vector-stores").list()?.forEach {
    include("vector-stores:$it")
}
