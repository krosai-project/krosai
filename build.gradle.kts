plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.realm) apply false
    alias(libs.plugins.mavenPublish)
}

// https://github.com/gradle/gradle/issues/22793
val lib = libs

val publishVersion = properties["publish.versions"] as String

val publishGroup = properties["publish.group"] as String

subprojects {

    apply(plugin = lib.plugins.kotlinMultiplatform.get().pluginId)
    apply(plugin = lib.plugins.kotlinxSerialization.get().pluginId)

    // 'krosai-sample' used androidApplication plugin,
    // Publishing of application and test variants is not supported
    if (name != ("krosai-sample")) {
        apply(plugin = lib.plugins.mavenPublish.get().pluginId)
        mavenPublishing {
            coordinates(publishGroup, name, publishVersion)
        }
    }

}
