package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface

private val timeSlots = listOf("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM")

@Composable
fun BookCallScreen(
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSubmit: (name: String, email: String, phone: String, date: String, time: String, topic: String?, notes: String?) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            SectionLabel("Book a Call")
        }

        Spacer(Modifier.height(8.dp))
        Text("Talk to a licensed agent", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        @Composable
        fun field(value: String, onChange: (String) -> Unit, label: String, keyboard: KeyboardType = KeyboardType.Text) {
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                label = { Text(label, color = BossMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                modifier = Modifier.fillMaxWidth(),
                colors = bookColors(),
            )
            Spacer(Modifier.height(10.dp))
        }

        field(name, { name = it }, "Full name *")
        field(email, { email = it }, "Email *", KeyboardType.Email)
        field(phone, { phone = it }, "Phone *", KeyboardType.Phone)
        field(date, { date = it }, "Date * (YYYY-MM-DD)")
        field(topic, { topic = it }, "Topic (optional)")
        field(notes, { notes = it }, "Notes (optional)")

        Text("Preferred time", color = BossMuted, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            timeSlots.take(4).forEach { slot ->
                androidx.compose.material3.Button(
                    onClick = { time = slot },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (time == slot) BossGold else BossSurface,
                        contentColor = if (time == slot) Color.Black else Color.White,
                    ),
                ) { Text(slot, style = MaterialTheme.typography.labelSmall) }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            timeSlots.drop(4).forEach { slot ->
                androidx.compose.material3.Button(
                    onClick = { time = slot },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (time == slot) BossGold else BossSurface,
                        contentColor = if (time == slot) Color.Black else Color.White,
                    ),
                ) { Text(slot, style = MaterialTheme.typography.labelSmall) }
            }
        }

        (localError ?: error)?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(20.dp))
        DisclosureText("Source tag: android_app_book_call")
        Spacer(Modifier.height(12.dp))
        BossPrimaryButton(
            text = "Book appointment",
            loading = loading,
            onClick = {
                localError = null
                when {
                    name.trim().length < 2 -> localError = "Enter your name"
                    !email.contains("@") -> localError = "Enter a valid email"
                    phone.filter { it.isDigit() }.length < 7 -> localError = "Enter a valid phone"
                    date.isBlank() -> localError = "Enter a date"
                    time.isBlank() -> localError = "Pick a time slot"
                    else -> onSubmit(name.trim(), email.trim(), phone.trim(), date.trim(), time, topic.ifBlank { null }, notes.ifBlank { null })
                }
            },
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun bookColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BossGold,
    unfocusedBorderColor = BossBorder,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = BossGold,
    unfocusedLabelColor = BossMuted,
    cursorColor = BossGold,
    focusedContainerColor = BossSurface,
    unfocusedContainerColor = BossSurface,
)
