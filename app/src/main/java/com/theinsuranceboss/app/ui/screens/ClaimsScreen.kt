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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import com.theinsuranceboss.app.BuildConfig
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.BossOutlineButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted

private val steps = listOf(
    "Safety first" to "Move to a safe location, check for injuries, and call 911 if anyone is hurt.",
    "Call the police" to "A police report strengthens your claim — get the report number.",
    "Document everything" to "Photos of vehicles, scene, plates, and the other driver's license + insurance card.",
    "Exchange info" to "Name, phone, carrier, policy number, driver's license, plate for all parties.",
    "Notify your carrier" to "Call your insurer or agent as soon as possible to open a claim.",
    "Talk to your agent" to "We'll walk you through the claim and advocate for you.",
)

@Composable
fun ClaimsScreen(onBack: () -> Unit) {
    val context = LocalContext.current

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
            SectionLabel("Claims Guide")
        }

        Spacer(Modifier.height(8.dp))
        Text("After an accident", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "Stay calm and work through these steps. Your agent is one tap away.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))

        steps.forEachIndexed { index, (title, body) ->
            BossCard(Modifier.padding(bottom = 12.dp)) {
                SectionLabel("Step ${index + 1}")
                Spacer(Modifier.height(8.dp))
                Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(body, color = BossMuted, style = MaterialTheme.typography.bodyMedium)
            }
        }

        BossCard {
            SectionLabel("Need help now?")
            Spacer(Modifier.height(12.dp))
            BossOutlineButton(
                text = "Call ${BuildConfig.PHONE_DISPLAY}",
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${BuildConfig.PHONE_DIAL}"))
                    runCatching { context.startActivity(intent) }
                },
            )
        }

        Spacer(Modifier.height(16.dp))
        DisclosureText("This guide is educational and not legal advice. Follow your carrier's claim instructions.")
        Spacer(Modifier.height(24.dp))
    }
}
