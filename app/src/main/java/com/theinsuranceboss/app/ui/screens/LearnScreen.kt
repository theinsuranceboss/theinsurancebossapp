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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.data.api.ContentItem
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.EmptyState
import com.theinsuranceboss.app.ui.components.ErrorState
import com.theinsuranceboss.app.ui.components.LoadingState
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.viewmodel.UiState

private val fallbackTips = listOf(
    ContentItem(
        id = "f1",
        title = "Bundle without overbuying",
        body = "Bundling auto + home often saves money, but review each line so you are not paying for coverage you do not need.",
        category = "Tips",
    ),
    ContentItem(
        id = "f2",
        title = "Re-shop every year",
        body = "Rates change. A quick annual review with a licensed agent can catch better pricing or gaps in coverage.",
        category = "Tips",
    ),
    ContentItem(
        id = "f3",
        title = "Document valuable property",
        body = "Keep receipts and photos for jewelry, electronics, and collectibles. Ask about scheduling high-value items.",
        category = "Tips",
    ),
    ContentItem(
        id = "f4",
        title = "Know your deductibles",
        body = "Higher deductibles lower premiums but raise out-of-pocket risk. Pick a number you can afford on a bad day.",
        category = "Basics",
    ),
)

@Composable
fun LearnScreen(
    contentState: UiState<List<ContentItem>>,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    val items = contentState.data?.takeIf { it.isNotEmpty() } ?: fallbackTips

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
            SectionLabel("Learn")
        }

        Spacer(Modifier.height(8.dp))
        Text("Tips from the Boss", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            if (contentState.error != null) "Showing offline tips — backend unavailable."
            else "Smart coverage, serious protection.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))

        when {
            contentState.loading && contentState.data == null -> LoadingState("Loading tips…")
            contentState.error != null && contentState.data == null -> {
                ErrorState(contentState.error, onRetry = onRetry)
                Spacer(Modifier.height(12.dp))
                items.forEach { item -> tipCard(item) }
            }
            items.isEmpty() -> EmptyState("No tips published yet.")
            else -> items.forEach { item -> tipCard(item) }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun tipCard(item: ContentItem) {
    BossCard(Modifier.padding(bottom = 12.dp)) {
        item.category?.let {
            SectionLabel(it)
            Spacer(Modifier.height(6.dp))
        }
        Text(item.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(item.body, color = BossMuted, style = MaterialTheme.typography.bodyMedium)
    }
}
