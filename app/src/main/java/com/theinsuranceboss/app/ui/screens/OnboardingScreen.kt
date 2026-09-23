package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.nav.Routes
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import kotlinx.coroutines.launch

private data class OnboardPage(val title: String, val body: String)

private val pages = listOf(
    OnboardPage(
        "Coverage that works as hard as you do",
        "Personal and commercial insurance from The Insurance Boss — shop A-rated carriers with a licensed team.",
    ),
    OnboardPage(
        "Quote. Audit. Protect.",
        "Get a free quote, upload your policy for a free audit, or calculate how much coverage you actually need.",
    ),
    OnboardPage(
        "Talk to a real agent",
        "Call or text a licensed agent anytime. No signup wall before you get value.",
    ),
)

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val last = pagerState.currentPage == pages.lastIndex

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("THE INSURANCE BOSS", color = BossGold, style = MaterialTheme.typography.labelMedium)
            TextButton(onClick = onDone) { Text("Skip", color = BossMuted) }
        }

        Spacer(Modifier.height(24.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { index ->
            val page = pages[index]
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(BossGold.copy(alpha = 0.15.dp.value.coerceAtMost(1f).let { 0.15f })),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("${index + 1}", color = BossGold, style = MaterialTheme.typography.displayMedium)
                }
                Spacer(Modifier.height(32.dp))
                Text(
                    page.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    page.body,
                    color = BossMuted,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pages.size) { i ->
                val selected = i == pagerState.currentPage
                Box(
                    Modifier
                        .padding(4.dp)
                        .size(if (selected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (selected) BossGold else BossMuted.copy(alpha = 0.4f)),
                )
            }
        }

        BossPrimaryButton(
            text = if (last) "Get Started" else "Next",
            onClick = {
                if (last) onDone()
                else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            },
        )
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onDone, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("I already know what I need", color = BossMuted, style = MaterialTheme.typography.bodySmall)
        }
    }
}
