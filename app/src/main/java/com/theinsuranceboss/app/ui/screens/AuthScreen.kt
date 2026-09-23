package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.theinsuranceboss.app.ui.viewmodel.UiState

@Composable
fun AuthScreen(
    authState: UiState<Boolean>,
    userName: String?,
    onBack: () -> Unit,
    onLogin: (login: String, password: String) -> Unit,
    onSignup: (username: String, email: String, password: String, fullName: String?) -> Unit,
    onReset: () -> Unit,
    onLogout: () -> Unit,
    onDone: () -> Unit,
) {
    var mode by remember { mutableStateOf(if (userName == null) "login" else "account") }
    var showPassword by remember { mutableStateOf(false) }

    var loginId by remember { mutableStateOf("") }
    var signupUser by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    onReset()
                    localError = null
                    if (mode == "signup") mode = "login" else onBack()
                },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            SectionLabel(
                when (mode) {
                    "signup" -> "Create account"
                    "account" -> "Account"
                    else -> "Login"
                },
            )
        }

        Spacer(Modifier.height(8.dp))

        if (mode == "account" && userName != null) {
            BossCard {
                Text(userName, color = Color.White, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Signed in · same account as theinsuranceboss.com",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(16.dp))
                BossPrimaryButton(
                    text = "Done",
                    onClick = {
                        onReset()
                        onDone()
                    },
                )
                Spacer(Modifier.height(10.dp))
                BossPrimaryButton(
                    text = "Log out",
                    onClick = {
                        onLogout()
                        onReset()
                        mode = "login"
                    },
                )
            }
            return
        }

        Text(
            if (mode == "signup") "Create your account" else "Welcome back",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Quote, audit, and wallet work as a guest. Login unlocks your policy wallet and referral code.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))

        if (mode == "login") {
            authField(loginId, { loginId = it }, "Email or username *", KeyboardType.Email)
            passwordField(password, { password = it }, showPassword, { showPassword = !showPassword })
        } else {
            authField(signupUser, { signupUser = it }, "Username *")
            authField(email, { email = it }, "Email *", KeyboardType.Email)
            authField(fullName, { fullName = it }, "Full name (optional)")
            passwordField(password, { password = it }, showPassword, { showPassword = !showPassword })
        }

        (localError ?: authState.error)?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(16.dp))
        DisclosureText("Source tag applies to leads only — login itself is account access.")
        Spacer(Modifier.height(12.dp))

        BossPrimaryButton(
            text = if (mode == "login") "Login" else "Create account",
            loading = authState.loading,
            onClick = {
                localError = null
                if (mode == "login") {
                    when {
                        loginId.isBlank() -> localError = "Enter email or username"
                        password.length < 6 -> localError = "Password must be at least 6 characters"
                        else -> onLogin(loginId.trim(), password)
                    }
                } else {
                    when {
                        signupUser.trim().length < 3 -> localError = "Username must be at least 3 characters"
                        !email.contains("@") || !email.contains(".") -> localError = "Enter a valid email"
                        password.length < 8 -> localError = "Password must be at least 8 characters"
                        else -> onSignup(signupUser.trim(), email.trim(), password, fullName.ifBlank { null })
                    }
                }
                if (authState.data == true) {
                    onReset()
                    onDone()
                }
            },
        )

        Spacer(Modifier.height(8.dp))
        Text(
            if (mode == "login") "New here? Create an account" else "Already have an account? Login",
            color = BossGold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onReset()
                    localError = null
                    password = ""
                    mode = if (mode == "login") "signup" else "login"
                }
                .padding(vertical = 8.dp),
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun authField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    keyboard: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, color = BossMuted) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = authColors(),
    )
}

@Composable
private fun passwordField(
    value: String,
    onChange: (String) -> Unit,
    visible: Boolean,
    toggle: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text("Password *", color = BossMuted) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = toggle) {
                Icon(
                    if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Hide password" else "Show password",
                    tint = BossMuted,
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = authColors(),
    )
}

@Composable
private fun authColors() = OutlinedTextFieldDefaults.colors(
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
