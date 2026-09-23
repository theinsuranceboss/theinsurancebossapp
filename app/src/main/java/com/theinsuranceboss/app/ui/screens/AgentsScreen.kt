package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface
import com.theinsuranceboss.app.ui.viewmodel.UiState

@Composable
fun AgentsScreen(
    requestState: UiState<Boolean>,
    onBack: () -> Unit,
    onSubmit: (name: String, email: String, phone: String?, notes: String?) -> Unit,
    onReset: () -> Unit,
    onOpenLogin: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BossGold)
        }

        SectionLabel("For Agents")
        Spacer(Modifier.height(8.dp))
        Text(
            "Request access",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Join the agent hub on theinsuranceboss.com — same tools, same account.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))

        @Composable
        fun fieldColors() = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BossGold,
            unfocusedBorderColor = BossBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = BossGold,
            focusedContainerColor = BossSurface,
            unfocusedContainerColor = BossSurface,
            focusedLabelColor = BossGold,
            unfocusedLabelColor = BossMuted,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone (optional)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Agency / notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
        )

        val err = localError ?: requestState.error
        if (err != null) {
            Spacer(Modifier.height(12.dp))
            Text(err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(20.dp))
        if (requestState.data == true) {
            Text(
                "Request received. An admin will review your access.",
                color = BossGold,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(12.dp))
            BossPrimaryButton(text = "Back to home", onClick = { onReset(); onBack() })
        } else {
            BossPrimaryButton(
                text = "Request access",
                loading = requestState.loading,
                onClick = {
                    localError = null
                    if (name.isBlank() || email.isBlank()) {
                        localError = "Name and email are required"
                        return@BossPrimaryButton
                    }
                    onSubmit(name.trim(), email.trim(), phone.trim().ifBlank { null }, notes.trim().ifBlank { null })
                },
            )
        }

        Spacer(Modifier.height(16.dp))
        BossPrimaryButton(
            text = "I already have an account — log in",
            onClick = onOpenLogin,
        )
        Spacer(Modifier.height(16.dp))
        DisclosureText("Agents share the same credentials as the web agent hub.")
        Spacer(Modifier.height(24.dp))
    }
}
