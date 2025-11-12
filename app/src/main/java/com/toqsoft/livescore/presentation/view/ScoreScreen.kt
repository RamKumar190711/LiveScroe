package com.toqsoft.livescore.presentation.view

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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ScoreScreen(viewModel: ScoreViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val allScores = (uiState as UiState.Success).scores

            val upcomingMatches = allScores.getUpcomingMatches()
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
                        ScoreItem(score)
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
                        ScoreItem(score)
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

// Extension function to filter upcoming matches
fun List<CricketScore>.getUpcomingMatches(): List<CricketScore> {
    val now = ZonedDateTime.now()
    return this.filter { match ->
        match.dateTimeGMT?.let { dateStr ->
            try {
                // Try parsing ISO format
                val matchTime = ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME)
                matchTime.isAfter(now)
            } catch (e: DateTimeParseException) {
                false
            }
        } ?: false
    }
}
