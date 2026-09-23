package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface

private val needs = listOf(
    "Life" to "Protect income & family",
    "Commercial" to "Business & liability",
    "Auto" to "Personal or fleet vehicles",
    "Home" to "Property & dwelling",
)

@Composable
fun NeedsScreen(onContinue: (String) -> Unit, onSkip: () -> Unit) {
    var selected by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
    ) {
        SectionLabel("Step 1 of 1")
        Spacer(Modifier.height(8.dp))
        Text(
            "What do you need?",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Pick a starting point — you can explore everything either way.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(needs) { (title, sub) ->
                val isSelected = selected == title
                Column(
                    Modifier
                        .height(140.dp)
                        .background(
                            if (isSelected) BossGold.copy(alpha = 0.12f) else BossSurface,
                            RoundedCornerShape(16.dp),
                        )
                        .then(
                            if (isSelected) Modifier.background(Color.Transparent) else Modifier
                        )
                        .clickable { selected = title }
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        title,
                        color = if (isSelected) BossGold else Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        sub,
                        color = BossMuted,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        BossPrimaryButton(
            text = if (selected != null) "Continue with $selected" else "Continue",
            enabled = selected != null,
            onClick = { selected?.let(onContinue) ?: onSkip() },
        )
        Spacer(Modifier.height(4.dp))
        BossPrimaryButton(text = "Explore as guest", onClick = onSkip)
    }
}
