# Krosai-Sample Module

## Overview

This is a sample module that demonstrates how to use the Krosai with a simple multi-platform chat application.

## Dependencies

Here are some of the main dependencies that are included :

- UI: using `Compose` for building the UI.
- DI: using `Koin` for dependency injection.
- Network: using `Ktor` for network operations.
- AIModule: By using `Krosai-openai` will `openai` as the AI model.

## Usage

To run the sample, it requires you to provide the OpenAI API key in a file named LocalData.kt created under "
krosai-sample/src/commonMain/kotlin":

 ```kotlin
package org.krosai.sample

object LocalData {
    const val API_KEY = "YOUR_API_KEY"
    const val BASE_URL = "https://api.openai.com"
}
 ```

### Desktop

To run the desktop application, execute the following command:

```shell
./gradlew :krosai-sample:desktopRun -DmainClass=org.krosai.sample.MainKt --quiet
```

### Android

To run the Android application, execute the following command:

```shell
./gradlew :krosai-sample:assembleDebug
adb install build/outputs/apk/debug/krosai-sample-debug.apk
adb shell am start -n "org.krosai/.sample.MainActivity"
```

### WASM

To run the WASM application, execute the following command:

```shell
./gradlew :krosai-sample:wasmJsBrowserRun
```
