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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
// field chips use Button; OutlinedTextField only for later steps
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface

private val coverageTypes = listOf("Auto", "Home", "Life", "Commercial")
private val coverageDetails = listOf(
    "What do you need coverage for?",
    "Tell us a bit more",
    "About you",
    "Contact details",
)

@Composable
fun QuoteScreen(
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSubmit: (name: String, email: String, phone: String?, coverageType: String?, notes: String?) -> Unit,
) {
    var step by remember { mutableIntStateOf(0) }
    var coverage by remember { mutableStateOf("") }
    var vehicles by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    val emailValid = email.contains("@") && email.contains(".")
    val phoneValid = phone.filter { it.isDigit() }.length >= 7
    val nameValid = name.trim().length >= 2

    fun next() {
        localError = null
        when (step) {
            0 -> if (coverage.isBlank()) localError = "Select a coverage type" else step++
            1 -> if (vehicles.isBlank() && zip.isBlank()) localError = "Add a detail to continue" else step++
            2 -> step++ // contact is final step only
            3 -> {
                if (!nameValid) localError = "Enter your full name"
                else if (!emailValid) localError = "Enter a valid email"
                else if (!phoneValid) localError = "Enter a valid phone"
                else onSubmit(name.trim(), email.trim(), phone.trim(), coverage, listOf(vehicles, zip).filter { it.isNotBlank() }.joinToString(", "))
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (step == 0) onBack() else step-- }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BossGold)
            }
            SectionLabel("Instant Quote · Step ${step + 1} of 4")
        }

        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (step + 1) / 4f },
            modifier = Modifier.fillMaxWidth(),
            color = BossGold,
            trackColor = BossSurface,
        )

        Spacer(Modifier.height(24.dp))
        Text(
            coverageDetails[step],
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(20.dp))

        when (step) {
            0 -> {
                coverageTypes.forEach { type ->
                    val selected = coverage == type
                    androidx.compose.material3.Button(
                        onClick = { coverage = type },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .height(52.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (selected) BossGold else BossSurface,
                            contentColor = if (selected) Color.Black else Color.White,
                        ),
                    ) {
                        Text(type, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            1 -> {
                OutlinedTextField(
                    value = vehicles,
                    onValueChange = { vehicles = it },
                    label = { Text("Vehicles / property (optional)", color = BossMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = zip,
                    onValueChange = { zip = it },
                    label = { Text("ZIP code", color = BossMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
            }
            2 -> {
                Text(
                    "We'll use your contact details only on the next step so a licensed agent can reach you.",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(12.dp))
                DisclosureText("No coverage is bound until a carrier issues a policy.")
            }
            3 -> {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full name *", color = BossMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email *", color = BossMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone *", color = BossMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(12.dp))
                DisclosureText("Availability may vary by state. We do not guarantee approval.")
            }
        }

        (localError ?: error)?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))
        BossPrimaryButton(
            text = if (step == 3) "Submit Quote Request" else "Continue",
            loading = loading,
            onClick = { next() },
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
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
