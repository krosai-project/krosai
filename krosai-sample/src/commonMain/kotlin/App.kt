package io.github.krosai.client.ai.samples

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.FilledIconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.krosai.client.ai.core.chat.client.ChatClient
import io.github.krosai.client.ai.samples.data.ChatMessage
import io.github.krosai.client.ai.samples.data.ChatPath
import io.github.krosai.client.ai.samples.data.LocaleChatPath
import io.github.krosai.client.ai.samples.di.AIModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject


@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(AIModule)
        }
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    ChatBar()
                    ChatScreen()
                }
            }
        }
    }

}

@Composable
fun RowScope.ChatScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val messages = remember {
            mutableStateListOf<ChatMessage>().apply {
                add(ChatMessage(mutableStateOf("Hello"), false))
            }
        }
        val lazyState = rememberLazyListState()

        ChatMessageArea(messages, lazyState)
        ChatInputArea {
            messages.add(it)
            lazyState.animateScrollToItem(messages.size)
        }
    }
}

@Composable
fun RowScope.ChatBar() {
    var openBar by remember { mutableStateOf(false) }
    val barWidth by animateDpAsState(if (!openBar) 0.dp else 120.dp)
    var chatPath by LocaleChatPath.current

    Column(
        modifier = Modifier.fillMaxHeight().width(barWidth)
            .background(MaterialTheme.colors.primary),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!openBar) return@Column
        BarItem("Chat", chatPath == ChatPath.CHAT) {
            chatPath = ChatPath.CHAT
        }
        BarItem("Search", chatPath == ChatPath.SEARCH) {
            chatPath = ChatPath.SEARCH
        }
    }
    Icon(
        modifier = Modifier.size(24.dp, 36.dp).align(Alignment.CenterVertically).clickable {
            openBar = !openBar
        }.background(
            MaterialTheme.colors.primary, shape = RoundedCornerShape(
                topEnd = 12.dp,
                bottomEnd = 12.dp
            )
        ),
        tint = MaterialTheme.colors.onPrimary,
        imageVector = if (openBar) Icons.AutoMirrored.Filled.ArrowRight else Icons.AutoMirrored.Filled.ArrowLeft,
        contentDescription = null
    )
}

@Composable
private fun BarItem(name: String, isSelected: Boolean = false, onClick: () -> Unit) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
                MaterialTheme.shapes.medium
            ).background(
                if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary,
                MaterialTheme.shapes.medium
            ),
        onClick = onClick
    ) {
        Text(
            text = name,
            color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
        )
    }
}

@Composable
fun ColumnScope.ChatMessageArea(messages: List<ChatMessage>, lazyState: LazyListState) {
    LazyColumn(
        modifier = Modifier.weight(1f),
        state = lazyState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(messages) { message ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
//                 每次文字变化时，滚动到最底部
//                LaunchedEffect(message.textState.value){
//                    lazyState.animateScrollToItem(messages.size,222222)
//                }
                val contentColor =
                    if (message.isUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                ) {
                    Row(
                        modifier = Modifier
                            .align(if (message.isUser) Alignment.End else Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (message.isUser.not()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Top)
                                    .size(30.dp)
                                    .background(MaterialTheme.colors.secondary, CircleShape)
                                    .clip(CircleShape),
                                text = "AI",
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        MessageBox(message)
                        if (message.isUser) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(MaterialTheme.colors.primary, CircleShape)
                                    .clip(CircleShape)
                                    .align(Alignment.Top),
                                text = "ME",
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun MessageBox(message: ChatMessage) {
    Box(
        modifier = Modifier
            .widthIn(min = 30.dp, max = 400.dp)
            .background(
                color = if (message.isUser) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        if (message.textState.value.isNotEmpty()) {
            SelectionContainer {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = message.textState.value,
                )
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.Center),
                color = LocalContentColor.current
            )
        }
    }
}

@Composable
fun ColumnScope.ChatInputArea(sendMessage: suspend (ChatMessage) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val chatClient = koinInject<ChatClient>()
    Row(
        modifier = Modifier.padding(
            top = 8.dp,
            bottom = 8.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            modifier = Modifier.weight(0.8f).align(Alignment.Bottom),
            value = inputText,
            onValueChange = {
                inputText = it
            }
        )
        FilledIconButton(
            modifier = Modifier.weight(0.2f).align(Alignment.Bottom),
            onClick = {
                if (inputText.isEmpty()) return@FilledIconButton
                val userMessage = ChatMessage(mutableStateOf(inputText), true)
                scope.launch(Dispatchers.Default) {
                    handleMessage(sendMessage, userMessage, chatClient)
                }
                inputText = ""
            }) {
            Text(
                text = "Send",
                color = MaterialTheme.colors.onPrimary
            )
        }

    }

}

private suspend fun handleMessage(
    sendMessage: suspend (ChatMessage) -> Unit,
    userMessage: ChatMessage,
    chatClient: ChatClient
) {
    sendMessage(userMessage)
    val aiMessage = ChatMessage(mutableStateOf(""), false)
    sendMessage(aiMessage)
    chatClient.stream {
        userText = { userMessage.textState.value }
    }.collect {
        var aiText by aiMessage.textState
        aiText += it.content
    }
}


