package com.toqsoft.livescore.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toqsoft.livescore.domain.model.CricketScore
import com.toqsoft.livescore.presentation.viewmodel.ScoreViewModel
import com.toqsoft.livescore.presentation.viewmodel.UiState
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScoreScreen(viewModel: ScoreViewModel, onScoreClick: (CricketScore) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val allScores = (uiState as UiState.Success).scores
            val upcomingMatches = allScores.getUpcomingMatches().sortedBy {
                ZonedDateTime.parse(it.dateTimeGMT, DateTimeFormatter.ISO_DATE_TIME)
            }
            val liveMatches = allScores - upcomingMatches

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                if (liveMatches.isNotEmpty()) {
                    item {
                        Text(
                            text = "Live / Completed Matches",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(liveMatches) { score ->
                        ScoreItem(score = score, onClick = onScoreClick)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (upcomingMatches.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Upcoming Matches",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(upcomingMatches) { score ->
                        ScoreItem(score = score, onClick = onScoreClick)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = (uiState as UiState.Error).message)
            }
        }
    }
}


fun List<CricketScore>.getUpcomingMatches(): List<CricketScore> {
    val nowUtc = ZonedDateTime.now(ZoneOffset.UTC)
    println("Current UTC time: $nowUtc")

    return this.filter { match ->
        match.dateTimeGMT?.let { dateStr ->
            try {
                // Parse API time as UTC
                val matchTimeUtc = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME)
                    .atZone(ZoneOffset.UTC)
                val isUpcoming = matchTimeUtc.isAfter(nowUtc)
                println("Match: ${match.name}, date: $dateStr, UTC: $matchTimeUtc, isUpcoming: $isUpcoming")
                isUpcoming
            } catch (e: Exception) {
                println("Failed to parse ${match.name}: $dateStr")
                false
            }
        } ?: false
    }
}
