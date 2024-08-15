import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

kotlin {

    js(IR) {
        nodejs()
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        nodejs()
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        commonMain.dependencies {
            api(projects.krosaiCore)
        }


        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)

            implementation(libs.ktor.client.logging)
        }

        jvmTest.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

    }
}
