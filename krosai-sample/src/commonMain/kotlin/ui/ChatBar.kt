package org.krosai.sample.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.krosai.sample.data.ChatPath
import org.krosai.sample.data.LocaleChatPath

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
