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
        // support for ktSearch
        maven("https://maven.tryformation.com/releases") {
            content {
                includeGroup("com.jillesvangurp")
                includeGroup("com.tryformation")
                includeGroup("com.tryformation.fritz2")
            }
        }
        mavenCentral()
    }
}

include(":krosai-core")
include(":krosai-sample")

rootProject.projectDir.resolve("models").list()
    ?.filter { it.startsWith("krosai-") }
    ?.forEach {
        include(":models:$it")
    }

rootProject.projectDir.resolve("vector-stores").list()
    ?.filter { it.startsWith("krosai-") }
    ?.forEach {
        include(":vector-stores:$it")
    }
