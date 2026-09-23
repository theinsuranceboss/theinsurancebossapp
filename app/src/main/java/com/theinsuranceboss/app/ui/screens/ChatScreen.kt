package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface
import com.theinsuranceboss.app.ui.viewmodel.MainViewModel

@Composable
fun ChatScreen(
    chatItems: List<MainViewModel.ChatItem>,
    thinking: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSend: (String) -> Unit,
    onReset: () -> Unit,
    onUserReady: (name: String, email: String) -> Unit,
) {
    var input by remember { mutableStateOf("") }
    var step by remember { mutableStateOf("fullName") }
    var fullName by remember { mutableStateOf("") }
    var localItems by remember { mutableStateOf<List<MainViewModel.ChatItem>>(emptyList()) }
    val listState = rememberLazyListState()
    val displayItems = localItems + chatItems

    LaunchedEffect(Unit) {
        if (chatItems.isEmpty()) onReset()
    }

    LaunchedEffect(displayItems.size, thinking) {
        val last = if (thinking) displayItems.size else displayItems.size - 1
        if (last >= 0) listState.animateScrollToItem(last)
    }

    fun submit() {
        val text = input.trim()
        if (text.isEmpty()) return
        input = ""
        when (step) {
            "fullName" -> {
                fullName = text
                step = "email"
                localItems = localItems + MainViewModel.ChatItem("user", text) +
                    MainViewModel.ChatItem(
                        "bot",
                        "Great to meet you, $text! And what is your Email Address so we can keep in touch?",
                    )
            }
            "email" -> {
                val first = fullName.split(" ").firstOrNull() ?: "there"
                step = "ready"
                onUserReady(fullName, text)
                localItems = localItems + MainViewModel.ChatItem("user", text) +
                    MainViewModel.ChatItem(
                        "bot",
                        "Hello $first, I'm the Insurance Boss. I'm here to help you navigate Commercial, Life, Personal, and Retirement insurance. How can I help you today?",
                    )
            }
            else -> onSend(text)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .imePadding(),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            Column(Modifier.weight(1f)) {
                Text("Insurance Boss", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Text("Always online", color = BossMuted, style = MaterialTheme.typography.bodySmall)
            }
            Box(
                Modifier
                    .size(10.dp)
                    .background(BossGold, RoundedCornerShape(50)),
            )
            Spacer(Modifier.size(12.dp))
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            if (displayItems.isEmpty() && !thinking) {
                item {
                    Text(
                        "Welcome to The Insurance Boss! I'm here to help you get the best coverage. To get started, what is your Full Name?",
                        color = BossMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BossSurface, RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                            .padding(14.dp),
                    )
                }
            }
            items(displayItems) { msg ->
                val isUser = msg.role == "user"
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart,
                ) {
                    Text(
                        msg.text,
                        color = if (isUser) Color.Black else Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .background(
                                if (isUser) BossGold else BossSurface,
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp,
                                ),
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                    )
                }
            }
            if (thinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(BossSurface, RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                            .padding(14.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = BossGold,
                            strokeWidth = 2.dp,
                        )
                        Spacer(Modifier.size(10.dp))
                        Text("Insurance Boss is thinking…", color = BossMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            if (error != null) {
                item {
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        when (step) {
                            "fullName" -> "Your full name…"
                            "email" -> "Your email…"
                            else -> "Ask about insurance…"
                        },
                        color = BossMuted,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BossGold,
                    unfocusedBorderColor = BossBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = BossGold,
                    focusedContainerColor = BossSurface,
                    unfocusedContainerColor = BossSurface,
                ),
            )
            Spacer(Modifier.size(8.dp))
            IconButton(
                onClick = { submit() },
                enabled = input.isNotBlank() && !thinking,
                modifier = Modifier
                    .size(48.dp)
                    .background(BossGold, RoundedCornerShape(12.dp)),
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.Black)
            }
        }
    }
}
