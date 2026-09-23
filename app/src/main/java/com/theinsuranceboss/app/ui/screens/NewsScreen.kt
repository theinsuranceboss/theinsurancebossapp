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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.data.api.NewsArticleDto
import com.theinsuranceboss.app.data.api.NewsResponse
import com.theinsuranceboss.app.ui.components.BossCard
import com.theinsuranceboss.app.ui.components.ErrorState
import com.theinsuranceboss.app.ui.components.LoadingState
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.viewmodel.UiState

@Composable
fun NewsScreen(
    state: UiState<NewsResponse>,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
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
            Text("News", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(8.dp))
        SectionLabel("Synced with theinsuranceboss.com")
        Spacer(Modifier.height(16.dp))

        when {
            state.loading -> LoadingState("Loading news…")
            state.error != null -> ErrorState(state.error, onRetry)
            else -> {
                val articles = state.data?.articles.orEmpty()
                if (articles.isEmpty()) {
                    Text("No articles right now — check back soon.", color = BossMuted, style = MaterialTheme.typography.bodyMedium)
                } else {
                    articles.forEach { article ->
                        NewsCard(article)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun NewsCard(article: NewsArticleDto) {
    BossCard {
        article.category?.let {
            SectionLabel(it)
            Spacer(Modifier.height(6.dp))
        }
        Text(
            article.title.orEmpty(),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(6.dp))
        val body = article.excerpt ?: article.body ?: article.content ?: ""
        Text(
            body,
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
        val meta = listOfNotNull(article.date ?: article.publishedAt ?: article.createdAt, article.author)
            .joinToString(" · ")
        if (meta.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(meta, color = BossGold, style = MaterialTheme.typography.labelSmall)
        }
    }
}
