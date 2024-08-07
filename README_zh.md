# Krosai

Krosai是一个Kotlin多平台库，为交互AI模型提供了一个简单且易于使用的API。支持多种AI模型，包括OpenAI等.

### 功能

- **多平台**： Krosai 是一个 Kotlin 多平台库，支持 JVM、Android、iOS 和 WASM。
- **简洁的API**： Krosai 结合 Kotlin DSL 提供了简单易用的 API，用于与AI模型交互。
- **多AI模型** Krosai 支持多种人工智能模型，包括 OpenAI 等。

## 支持

### API

- **聊天**
    - call调用
    - stream调用
    - 提示词
    - 函数调用
    - 增强器
        - 聊天记忆存储
- **向量化**(计划中)
- **图像**(计划中)
- **音频转换**(计划中)
- **语音**(计划中)

### AI模型

- **OpenAI**

### 使用示例

> 以下将使用 OpenAI 模型进行演示,
> 其他模型的使用方法类似。

```kotlin
// 1. 创建一个 `ModelFactoryContext`
val context = buildModelFactoryContext {
    // 使用 OpenAI 模型
    factory(OpenAI) {
        baseUrl = "https://api.openai.com"
        apiKey = "输入你的API密钥"
    }
}
// 2. 从其中获取 `ModelFactory`
val modelFactory = context[OpenAI]
```

- **聊天**
    - **提示词**
      ```kotlin
      // 从 `ModelFactory` 创建一个 `ChatClient`
      val chatClient = modelFactory.createChatClient{
        // 设置 `ChatClient` 的默认请求
        // 例如，设置 `systemText`
        systemText = { "请用 ${get("language")}回答" } 
      }
      // 调用 `ChatClient` 的 `call` 或 `stream` 方法发送请求
      chatClient.call{
        // 为请求设置 `userText`
        userText = { "请叫我Kamo" }
        system { 
            // 在 systemText 中将 "language "填写为 "日语""
            "language" to "日语"
        }
      }.let { response ->
           // 打印响应内容
           println(response.content)
        }
      ```
    - **增强器**
      ```kotlin
      val chatClient = context.createChatClient {
          enhancers {
              // 将 `ChatMemoryEnhancer` 添加到 `ChatClient` 的默认请求中
              // `InMemoryMessageStore` 用于在内存中存储消息
              +MessageChatMemoryEnhancer(InMemoryMessageStore())
          }
      }
      chatClient.call {
          userText = { "请叫我Kamo" }
      }
      
      chatClient.call {
          userText = { "请用日语回答" }
      }.let {
          // 用日语回答
          println("content: ${it.content}")
          // 响应包含 "Kamo"，因为它存储在内存存储中
          assert(it.content.contains("Kamo"))
      }
      ```
    - **函数调用**
      > 定义一个 `FunctionCall` 并在请求中使用它。
      ```kotlin
      @SerialDescription("获取日期时间API的请求")
      @Serializable
      data class Request(
          @SerialName("location") 
          @SerialDescription("地点")
          val location: String
      )
      val dateTimeFun = buildFunctionCall {
          name = "date_time"
          description = "通过位置获取当前日期"
          withCall { req: Request ->
              "${req.location}中的当前时间是 ${LocalDateTime.now()}" 
          }
      }
      ```
      ```kotlin
      chatClient.call {
         userText = { "纽约现在几点？" }
         函数 {
             +dateTimeFun
         }
      .let {
        // 打印包含纽约当前时间的响应内容
         println(it.content)
      }
      ```