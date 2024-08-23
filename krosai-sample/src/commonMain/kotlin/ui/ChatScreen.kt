package org.krosai.sample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.krosai.core.chat.client.ChatClient
import org.krosai.sample.data.ChatMessage

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
                                .background(MaterialTheme.colorScheme.tertiary, CircleShape)
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


@Composable
fun MessageBox(message: ChatMessage) {
    Box(
        modifier = Modifier
            .widthIn(min = 30.dp)
            .background(
                color = when (message.isUser) {
                    true -> MaterialTheme.colorScheme.primaryContainer
                    false -> MaterialTheme.colorScheme.tertiaryContainer
                },
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
                        .align(Alignment.Center)
                )
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
fun ColumnScope.ChatInputArea(sendMessage: suspend (ChatMessage) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val chatClient = koinInject<ChatClient>()
    val doSend = {
        if (inputText.trim().isNotEmpty()) {
            val userMessage = ChatMessage(mutableStateOf(inputText), true)
            scope.launch(Dispatchers.Default) {
                handleMessage(sendMessage, userMessage, chatClient)
            }
            focusManager.moveFocus(FocusDirection.Down)
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
        userText { userMessage.textState.value }
    }.collect {
        var aiText by aiMessage.textState
        aiText += it.content
    }
}
