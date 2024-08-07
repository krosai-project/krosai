# Krosai

Krosai is a Kotlin Multiplatform library that provides a simple and easy-to-use API for interacting with AI models.
It supports multiple AI models, including OpenAI and more.

**[English](README.md) | [简体中文](README_zh.md)**

## Features

- **Multiplatform**: Krosai is a Kotlin Multiplatform library that supports JVM, Android, iOS and WASM.
- **Simple API**: Krosai combined with Kotlin DSL provides easy-to-use APIs for interacting with AI models.
- **Multiple AI Models**: Krosai supports multiple AI models, including OpenAI.

## Supported

### API

- **Chat**
    - Call
    - Stream
    - Prompt
    - FunctionCall
    - Enhancer
        - ChatMemory
- **Embedding**(TODO)
- **Image**(TODO)
- **AudioTranscription**(TODO)
- **Speech**(TODO)

### AI Models

- **OpenAI**

## Usage Example

> The following example uses the OpenAI model,
> and the other models are used in a similar way.

```kotlin
// 1. create a `ModelFactoryContext` 
val context = buildModelFactoryContext {
    // Using OpenAI Model
    factory(OpenAI) {
        baseUrl = "https://api.openai.com"
        apiKey = "YOUR_API_KEY"
    }
}
// 2. get a `ModelFactory` from the it
val modelFactory = context[OpenAI]
```

- **Chat**
    - **Prompt**
      ```kotlin
      // create a `ChatClient` from the `ModelFactory`
      val chatClient = modelFactory.createChatClient{
        // set the Default Request of the ChatClient
        // for example, set the `systemText`
        systemText = {"Please answer in ${get("language")}"}
      }
      // call or stream the `ChatClient` with the request
      chatClient.call{
        // set the `userText` for the request
        userText = { "Please call me Kamo." }
        system { 
            // Fill 'language' with "Japanese" in systemText"
            "language" to "Japanese"
        }
      }.let { response ->
           // print the response content
           println(response.content)
        }
      ```
    - **Enhancer**
      ```kotlin
      val chatClient = context.createChatClient {
          enhancers {
              // add the `ChatMemoryEnhancer` to the Default Request of the `ChatClient`
              // `InMemoryMessageStore` is used to store the messages in memory
              +MessageChatMemoryEnhancer(InMemoryMessageStore())
          }
      }
      chatClient.call {
          userText = { "Please call me Kamo." }
      }
      
      chatClient.call {
          userText = { "Please answer in Japanese" }
      }.let {
          // respond in japanese
          println("content: ${it.content}")
          // the response contains "Kamo", because it is stored in Memory
          assert(it.content.contains("Kamo"))
      }
      ```
    - **FunctionCall**
      > Define a `FunctionCall` and use it in the Request.
      ```kotlin
      @SerialDescription("get date time API Request")
      @Serializable
      data class Request(
          @SerialName("location") 
          @SerialDescription("location")
          val location: String
      )
      val dateTimeFun = buildFunctionCall {
          name = "date_time"
          description = "Get the current date by location"
          withCall { req: Request ->
              "the current time in ${req.location} is ${LocalDateTime.now()}"
          }
      }
      ```
      ```kotlin
      chatClient.call {
         userText = { "What time is it in New York?" }
         functions {
             +dateTimeFun
         }
      .let {
        // print the response content containing the current time in New York
         println(it.content)
      }
      ```