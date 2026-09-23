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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CalculatorScreen(
    savedCoverage: Double?,
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onRun: (income: Double, debts: Double, years: Int) -> Unit,
    onSaveReport: (name: String, email: String, phone: String?, coverage: Double, income: Double, debts: Double, years: Int) -> Unit,
    onReset: () -> Unit,
) {
    var income by remember { mutableFloatStateOf(75000f) }
    var debts by remember { mutableFloatStateOf(20000f) }
    var years by remember { mutableFloatStateOf(10f) }
    var showReport by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    var estimate by remember { mutableStateOf<Double?>(null) }

    val money = NumberFormat.getCurrencyInstance(Locale.US)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = BossGold)
            }
            SectionLabel("Coverage Calculator")
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "How much coverage do you need?",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(Modifier.height(24.dp))
        BossCard {
            Text("Annual income", color = BossMuted, style = MaterialTheme.typography.bodySmall)
            Text(money.format(income.toDouble()), color = BossGold, style = MaterialTheme.typography.headlineSmall)
            Slider(
                value = income,
                onValueChange = { income = it },
                valueRange = 20000f..500000f,
                steps = 48,
                colors = SliderDefaults.colors(thumbColor = BossGold, activeTrackColor = BossGold),
            )

            Spacer(Modifier.height(12.dp))
            Text("Total debts", color = BossMuted, style = MaterialTheme.typography.bodySmall)
            Text(money.format(debts.toDouble()), color = BossGold, style = MaterialTheme.typography.headlineSmall)
            Slider(
                value = debts,
                onValueChange = { debts = it },
                valueRange = 0f..500000f,
                steps = 50,
                colors = SliderDefaults.colors(thumbColor = BossGold, activeTrackColor = BossGold),
            )

            Spacer(Modifier.height(12.dp))
            Text("Years to cover", color = BossMuted, style = MaterialTheme.typography.bodySmall)
            Text("${years.toInt()} years", color = BossGold, style = MaterialTheme.typography.headlineSmall)
            Slider(
                value = years,
                onValueChange = { years = it },
                valueRange = 1f..40f,
                steps = 38,
                colors = SliderDefaults.colors(thumbColor = BossGold, activeTrackColor = BossGold),
            )
        }

        Spacer(Modifier.height(16.dp))
        BossPrimaryButton(
            text = "Calculate coverage need",
            onClick = {
                val calc = ((income * 0.7) + debts) * years
                estimate = calc
                onRun(income.toDouble(), debts.toDouble(), years.toInt())
            },
        )

        val display = savedCoverage ?: estimate
        if (display != null) {
            Spacer(Modifier.height(16.dp))
            BossCard {
                SectionLabel("Estimated coverage need")
                Spacer(Modifier.height(8.dp))
                Text(
                    money.format(display),
                    color = BossGold,
                    style = MaterialTheme.typography.displayMedium,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Educational estimate only — not a quote or offer of coverage.",
                    color = BossMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(16.dp))
                if (!showReport) {
                    BossPrimaryButton(
                        text = "Email me my full report",
                        onClick = { showReport = true },
                    )
                }
            }
        }

        if (showReport && display != null) {
            Spacer(Modifier.height(16.dp))
            BossCard {
                SectionLabel("Where should we send it?")
                Spacer(Modifier.height(12.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name *", color = BossMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = calcColors(),
                )
                Spacer(Modifier.height(10.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email *", color = BossMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = calcColors(),
                )
                Spacer(Modifier.height(10.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone (optional)", color = BossMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = calcColors(),
                )
                Spacer(Modifier.height(12.dp))
                DisclosureText("Source tag: android_app_calculator")
                Spacer(Modifier.height(12.dp))
                (localError ?: error)?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                }
                BossPrimaryButton(
                    text = "Send my full report",
                    loading = loading,
                    onClick = {
                        localError = null
                        when {
                            name.trim().length < 2 -> localError = "Enter your name"
                            !email.contains("@") -> localError = "Enter a valid email"
                            else -> onSaveReport(
                                name.trim(),
                                email.trim(),
                                phone.ifBlank { null },
                                display,
                                income.toDouble(),
                                debts.toDouble(),
                                years.toInt(),
                            )
                        }
                    },
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun calcColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BossGold,
    unfocusedBorderColor = com.theinsuranceboss.app.ui.theme.BossBorder,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = BossGold,
    unfocusedLabelColor = BossMuted,
    cursorColor = BossGold,
    focusedContainerColor = BossSurface,
    unfocusedContainerColor = BossSurface,
)
