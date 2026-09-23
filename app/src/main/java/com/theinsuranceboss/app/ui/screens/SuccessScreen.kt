package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted

@Composable
fun SuccessScreen(
    section: String,
    title: String,
    body: String,
    sourceTag: String?,
    primaryText: String,
    onPrimary: () -> Unit,
    secondaryText: String? = null,
    onSecondary: (() -> Unit)? = null,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(48.dp))
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = BossGold,
            modifier = Modifier.size(72.dp),
        )
        Spacer(Modifier.height(24.dp))
        SectionLabel(section)
        Spacer(Modifier.height(12.dp))
        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            body,
            color = BossMuted,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        if (sourceTag != null) {
            DisclosureText("Source tag: $sourceTag")
            Spacer(Modifier.height(24.dp))
        }
        Spacer(Modifier.weight(1f))
        BossPrimaryButton(text = primaryText, onClick = onPrimary)
        if (secondaryText != null && onSecondary != null) {
            Spacer(Modifier.height(10.dp))
            BossPrimaryButton(text = secondaryText, onClick = onSecondary)
        }
        Spacer(Modifier.height(24.dp))
    }
}
