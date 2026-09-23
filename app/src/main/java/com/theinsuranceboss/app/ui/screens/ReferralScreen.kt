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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface

@Composable
fun ReferralScreen(
    codeState: com.theinsuranceboss.app.ui.viewmodel.UiState<String>,
    submitState: com.theinsuranceboss.app.ui.viewmodel.UiState<Boolean>,
    isLogged: Boolean,
    onBack: () -> Unit,
    onLoadCode: () -> Unit,
    onLogin: () -> Unit,
    onSubmit: (code: String, name: String?, email: String?, phone: String?) -> Unit,
    onResetSubmit: () -> Unit,
) {
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            SectionLabel("Refer a Friend")
        }

        Spacer(Modifier.height(8.dp))
        Text("Share the wealth", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "Give a friend your code. Rewards are disabled by default and subject to state anti-rebating laws.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))

        if (isLogged) {
            BossCard {
                SectionLabel("Your referral code")
                Spacer(Modifier.height(12.dp))
                when {
                    codeState.loading -> Text("Loading code…", color = BossMuted)
                    codeState.error != null -> {
                        Text(codeState.error, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        BossPrimaryButton(text = "Retry", onClick = onLoadCode)
                    }
                    codeState.data != null -> {
                        Text(
                            codeState.data,
                            color = BossGold,
                            style = MaterialTheme.typography.displayMedium,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Share this code when a friend contacts us.",
                            color = BossMuted,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    else -> BossPrimaryButton(text = "Get my code", onClick = onLoadCode)
                }
            }
            Spacer(Modifier.height(16.dp))
        } else {
            BossCard {
                Text("Login to get your code", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Referral codes are tied to your account (same as theinsuranceboss.com).",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(16.dp))
                BossPrimaryButton(text = "Login / Sign up", onClick = onLogin)
            }
            Spacer(Modifier.height(16.dp))
        }

        BossCard {
            SectionLabel("Submit a friend")
            Spacer(Modifier.height(12.dp))
            referralField(code, { code = it }, "Friend's referral code *")
            referralField(name, { name = it }, "Their name")
            referralField(email, { email = it }, "Their email", KeyboardType.Email)
            referralField(phone, { phone = it }, "Their phone", KeyboardType.Phone)

            (localError ?: submitState.error)?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            if (submitState.data == true) {
                Spacer(Modifier.height(8.dp))
                Text("Referral submitted — thank you!", color = com.theinsuranceboss.app.ui.theme.BossSuccess)
            }

            Spacer(Modifier.height(12.dp))
            DisclosureText("Source tag: android_app_referral")
            Spacer(Modifier.height(12.dp))
            BossPrimaryButton(
                text = "Submit referral",
                loading = submitState.loading,
                onClick = {
                    localError = null
                    when {
                        code.trim().length < 3 -> localError = "Enter a valid code"
                        email.isNotBlank() && !email.contains("@") -> localError = "Enter a valid email"
                        else -> {
                            onResetSubmit()
                            onSubmit(
                                code.trim(),
                                name.ifBlank { null },
                                email.ifBlank { null },
                                phone.ifBlank { null },
                            )
                        }
                    }
                },
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun referralField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    keyboard: KeyboardType = KeyboardType.Text,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label, color = BossMuted) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboard),
            modifier = Modifier.fillMaxWidth(),
            colors = referralColors(),
        )
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun referralColors() = OutlinedTextFieldDefaults.colors(
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
