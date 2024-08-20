package org.krosai.sample

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.krosai.core.chat.client.ChatClient
import org.krosai.sample.data.ChatMessage
import org.krosai.sample.data.ChatPath
import org.krosai.sample.data.LocaleChatPath
import org.krosai.sample.di.AiModule


@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(AiModule)
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
            .background(MaterialTheme.colorScheme.primary),
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
            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(
                topEnd = 12.dp,
                bottomEnd = 12.dp
            )
        ),
        tint = MaterialTheme.colorScheme.onPrimary,
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
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.shapes.medium
            ).background(
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.medium
            ),
        onClick = onClick
    ) {
        Text(
            text = name,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
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
//                 Compose中怎么让LazyColumn自动保持在最下方
                CompositionLocalProvider(
                    LocalContentColor provides when (message.isUser) {
                        true -> MaterialTheme.colorScheme.onPrimary
                        false -> MaterialTheme.colorScheme.onSecondary
                    },
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
                                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
                                    .clip(CircleShape),
                                text = "AI",
                                color = MaterialTheme.colorScheme.onPrimary,
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
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .clip(CircleShape)
                                    .align(Alignment.Top),
                                text = "ME",
                                color = MaterialTheme.colorScheme.onPrimary,
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
            .widthIn(min = 30.dp)
            .background(
                color = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        val messageText by message.textState

        if (messageText.isNotEmpty()) {
            SelectionContainer {
                Markdown(
                    content = messageText,
                    modifier = Modifier
                        .padding(8.dp)
                        .widthIn(min = 30.dp, max = 500.dp)
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
    val doSend = {
        if (inputText.isNotEmpty()) {
            val userMessage = ChatMessage(mutableStateOf(inputText), true)
            scope.launch(Dispatchers.Default) {
                handleMessage(sendMessage, userMessage, chatClient)
            }
            inputText = ""
        }
    }
    Row(
        modifier = Modifier.padding(
            top = 8.dp,
            bottom = 8.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .weight(0.8f)
                .align(Alignment.Bottom)
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyUp && it.key == Key.Enter) {
                        doSend()
                        true
                    } else {
                        false
                    }
                },
            value = inputText,
            onValueChange = {
                inputText = it
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
            ),
            keyboardActions = KeyboardActions(
                onSend = { doSend() },
            ),
            shape = MaterialTheme.shapes.medium,
        )
        FilledIconButton(
            modifier = Modifier.weight(0.2f).align(Alignment.Bottom),
            onClick = doSend
        ) {
            Text(
                text = "Send",
                color = MaterialTheme.colorScheme.onPrimary
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


