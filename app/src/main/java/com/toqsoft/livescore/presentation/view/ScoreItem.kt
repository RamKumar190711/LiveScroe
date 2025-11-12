package com.toqsoft.livescore.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.toqsoft.livescore.domain.model.CricketScore
import com.toqsoft.livescore.domain.utill.getFlagUrl
import java.time.ZonedDateTime

@Composable
fun ScoreItem(score: CricketScore) {

    val context = LocalContext.current

    val matchTypeColor = when (score.matchType?.lowercase()) {
        "t20" -> Color.Red
        "odi" -> Color.Gray
        "test" -> Color.Black
        else -> MaterialTheme.colorScheme.primary
    }

    // Coil image loader with SVG support
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Top row: match name + type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(text = score.name, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(matchTypeColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = score.matchType ?: "Test",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Teams and scores / upcoming start time
            Column(modifier = Modifier.fillMaxWidth()) {
                score.teams.forEachIndexed { index, team ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val flagUrl = getFlagUrl(team)
                        if (flagUrl.isNotEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(flagUrl)
                                    .crossfade(true)
                                    .build(),
                                imageLoader = imageLoader,
                                contentDescription = "$team flag/logo",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(
                            text = team,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )

                        val teamScore = score.score?.getOrNull(index)
                        Text(
                            text = teamScore?.let { "${it.r}/${it.w}" } ?: score.dateTimeGMT?.let {
                                val displayTime = try {
                                    ZonedDateTime.parse(it).toLocalTime().toString()
                                } catch (e: Exception) {
                                    it
                                }
                                "Starts at: $displayTime UTC"
                            } ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            // Match status
            score.status?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
