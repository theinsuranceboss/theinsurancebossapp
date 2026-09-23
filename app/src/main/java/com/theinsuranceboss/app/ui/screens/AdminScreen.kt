package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.data.api.LeadDto
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface
import com.theinsuranceboss.app.ui.viewmodel.UiState

@Composable
fun AdminScreen(
    leadsState: UiState<List<LeadDto>>,
    onBack: () -> Unit,
    onLoad: (password: String) -> Unit,
    onReset: () -> Unit,
) {
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var unlocked by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                if (unlocked) {
                    unlocked = false
                    password = ""
                    onReset()
                } else {
                    onBack()
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            Text("Admin panel", color = Color.White, style = MaterialTheme.typography.titleLarge)
        }

        if (!unlocked) {
            Column(Modifier.padding(20.dp)) {
                SectionLabel("Locked")
                Spacer(Modifier.height(8.dp))
                Text(
                    "Enter the admin password (same as the web admin panel).",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Admin password") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = BossMuted,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BossGold,
                        unfocusedBorderColor = BossBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = BossGold,
                        focusedContainerColor = BossSurface,
                        unfocusedContainerColor = BossSurface,
                        focusedLabelColor = BossGold,
                        unfocusedLabelColor = BossMuted,
                    ),
                )
                if (leadsState.error != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(leadsState.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                if (leadsState.error != null && unlocked) {
                    unlocked = false
                }
                Spacer(Modifier.height(20.dp))
                BossPrimaryButton(
                    text = "Unlock",
                    loading = leadsState.loading,
                    onClick = {
                        if (password.isNotBlank()) {
                            onReset()
                            onLoad(password)
                        }
                    },
                )
                LaunchedEffect(leadsState.data) {
                    if (leadsState.data != null) unlocked = true
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Tip: default password is insuranceboss",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        } else {
            Column(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp)) {
                SectionLabel("App leads")
                Spacer(Modifier.height(4.dp))
                Text(
                    "${leadsState.data?.size ?: 0} total · newest first",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            when {
                leadsState.loading -> {
                    Text(
                        "Loading leads…",
                        color = BossMuted,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                leadsState.error != null -> {
                    Column(Modifier.padding(20.dp)) {
                        Text(leadsState.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(12.dp))
                        BossPrimaryButton(text = "Retry", onClick = { onLoad(password) })
                    }
                }
                else -> {
                    val leads = leadsState.data.orEmpty()
                    if (leads.isEmpty()) {
                        Text(
                            "No leads yet.",
                            color = BossMuted,
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        LazyColumn(
                            Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                        ) {
                            items(leads) { lead ->
                                BossCard {
                                    Text(
                                        lead.name ?: lead.email ?: "Lead",
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    lead.email?.let {
                                        Text(it, color = BossGold, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Row {
                                        Text(
                                            lead.source ?: "",
                                            color = BossMuted,
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        lead.status?.let {
                                            Text(
                                                "  · $it",
                                                color = BossMuted,
                                                style = MaterialTheme.typography.labelSmall,
                                            )
                                        }
                                    }
                                    lead.phone?.let {
                                        Spacer(Modifier.height(2.dp))
                                        Text(it, color = BossMuted, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}
