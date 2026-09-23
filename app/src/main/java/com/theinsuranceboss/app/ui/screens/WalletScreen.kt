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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.data.api.PolicyDto
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.EmptyState
import com.theinsuranceboss.app.ui.components.ErrorState
import com.theinsuranceboss.app.ui.components.LoadingState
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.nav.Routes
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted

@Composable
fun WalletScreen(
    policiesState: com.theinsuranceboss.app.ui.viewmodel.UiState<List<PolicyDto>>,
    addState: com.theinsuranceboss.app.ui.viewmodel.UiState<Boolean>,
    isLogged: Boolean,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRefresh: () -> Unit,
    onAdd: (title: String, carrier: String?, number: String?, type: String?, premium: String?) -> Unit,
    onResetAdd: () -> Unit,
) {
    var showAdd by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var carrier by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var premium by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            SectionLabel("Policy Wallet")
        }

        Spacer(Modifier.height(8.dp))

        if (!isLogged) {
            BossCard {
                Icon(Icons.Default.Shield, contentDescription = null, tint = BossGold)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Login required",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Your wallet uses the same account as theinsuranceboss.com. Everything else in the app works as a guest.",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(16.dp))
                BossPrimaryButton(text = "Login / Sign up", onClick = onLogin)
            }
            return
        }

        if (!showAdd) {
            BossPrimaryButton(text = "Add policy", onClick = { showAdd = true })
            Spacer(Modifier.height(16.dp))
        } else {
            BossCard {
                SectionLabel("New policy")
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title *", color = BossMuted) }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = carrier, onValueChange = { carrier = it }, label = { Text("Carrier", color = BossMuted) }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Policy #", color = BossMuted) }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type (Auto, Home…)", color = BossMuted) }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = premium, onValueChange = { premium = it }, label = { Text("Premium", color = BossMuted) }, modifier = Modifier.fillMaxWidth())
                if (addState.error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(addState.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(12.dp))
                BossPrimaryButton(
                    text = "Save policy",
                    loading = addState.loading,
                    onClick = {
                        if (title.isNotBlank()) {
                            onAdd(
                                title.trim(),
                                carrier.ifBlank { null },
                                number.ifBlank { null },
                                type.ifBlank { null },
                                premium.ifBlank { null },
                            )
                            if (addState.data == true) {
                                showAdd = false
                                title = ""; carrier = ""; number = ""; type = ""; premium = ""
                                onResetAdd()
                            }
                        }
                    },
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        when {
            policiesState.loading -> LoadingState("Loading policies…")
            policiesState.error != null -> ErrorState(policiesState.error, onRetry = onRefresh)
            policiesState.data != null && policiesState.data.isEmpty() -> EmptyState("No policies yet. Add one manually or from a submitted audit.")
            policiesState.data != null -> LazyColumn(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)) {
                items(policiesState.data) { p ->
                    BossCard {
                        Text(p.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
                        p.carrier?.let { Text(it, color = BossGold, style = MaterialTheme.typography.bodyMedium) }
                        val meta = listOfNotNull(p.policyType, p.policyNumber, p.premium).joinToString(" · ")
                        if (meta.isNotBlank()) Text(meta, color = BossMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> LoadingState()
        }
    }
}
