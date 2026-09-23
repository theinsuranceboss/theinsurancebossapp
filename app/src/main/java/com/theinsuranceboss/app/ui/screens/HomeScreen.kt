package com.theinsuranceboss.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.BuildConfig
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.nav.Routes
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface

@Composable
fun HomeScreen(
    userName: String?,
    healthOk: Boolean?,
    onNav: (String) -> Unit,
) {
    val context = LocalContext.current

    fun callAgent() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${BuildConfig.PHONE_DIAL}"))
        runCatching { context.startActivity(intent) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                SectionLabel(if (userName.isNullOrBlank()) "Guest mode" else "Welcome back")
                Text(
                    userName ?: "The Insurance Boss",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Box(
                Modifier
                    .size(48.dp)
                    .background(BossGold, RoundedCornerShape(50))
                    .clickable { callAgent() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Call, contentDescription = "Call agent", tint = Color.Black)
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(
            when (healthOk) {
                true -> "Connected · Smart Coverage, Serious Protection"
                false -> "Offline features only — retrying backend…"
                else -> "Checking backend…"
            },
            color = BossMuted,
            style = MaterialTheme.typography.bodySmall,
        )

        Spacer(Modifier.height(20.dp))

        val entries = listOf(
            Triple("Get a Quote", "4-step instant quote", Icons.Default.RequestQuote to Routes.QUOTE),
            Triple("Free Policy Audit", "Upload declarations page", Icons.Default.CameraAlt to Routes.AUDIT),
            Triple("Coverage Calculator", "Sliders → full report", Icons.Default.Calculate to Routes.CALCULATOR),
            Triple("Call / Text an Agent", BuildConfig.PHONE_DISPLAY, Icons.Default.Call to "call"),
        )

        entries.chunked(2).forEach { rowItems ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { (title, sub, iconRoute) ->
                    val (icon, route) = iconRoute
                    BossCard(
                        Modifier
                            .weight(1f)
                            .height(150.dp)
                            .clickable {
                                if (route == "call") callAgent() else onNav(route)
                            },
                    ) {
                        Icon(icon, contentDescription = null, tint = BossGold, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(4.dp))
                        Text(sub, color = BossMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(8.dp))
        SectionLabel("More tools")

        val more = listOf(
            Triple("Policy Wallet", "Your policies in one place", Icons.Default.CardMembership to Routes.WALLET),
            Triple("Book a Call", "Pick a date & time", Icons.Default.Event to Routes.BOOK),
            Triple("Refer a Friend", "Share your code", Icons.Default.CardGiftcard to Routes.REFERRAL),
            Triple("Claims Guide", "After an accident", Icons.Default.Quiz to Routes.CLAIMS),
            Triple("Learn", "Tips from the Boss", Icons.Default.School to Routes.LEARN),
            Triple(if (userName == null) "Login / Sign up" else "Account", if (userName == null) "Same account as the web" else userName, Icons.Default.School to Routes.AUTH),
        )

        more.chunked(2).forEach { rowItems ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { (title, sub, iconRoute) ->
                    val (icon, route) = iconRoute
                    BossCard(
                        Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clickable { onNav(route) },
                    ) {
                        Icon(icon, contentDescription = null, tint = BossGold, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.height(10.dp))
                        Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(sub, color = BossMuted, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(8.dp))
        BossCard {
            SectionLabel("Disclosure")
            Spacer(Modifier.height(8.dp))
            Text(
                "Availability of products and eligibility may vary by state. A licensed agent will contact you. We do not guarantee approval — quotes are subject to underwriting and carrier guidelines.",
                color = BossMuted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}
