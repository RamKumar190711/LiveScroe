package com.toqsoft.livescore.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScoreItem(score: CricketScore, onClick: (CricketScore) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick(score) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top row: match name + type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(score.name, style = MaterialTheme.typography.bodyMedium)
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Gray)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(score.matchType ?: "Test", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Teams & scores
            score.teams.forEachIndexed { index, team ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val flagUrl = getFlagUrl(team)
                    if (flagUrl.isNotEmpty()) {
                        AsyncImage(
                            model = flagUrl,
                            contentDescription = "$team flag",
                            modifier = Modifier.size(24.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(team, modifier = Modifier.weight(1f))

                    val teamScore = score.score?.getOrNull(index)
                    val crr = if (teamScore != null && teamScore.o > 0) teamScore.r.toDouble() / teamScore.o else 0.0
                    Text(
                        text = teamScore?.let { "${it.r}/${it.w} (${it.o}) CRR: ${"%.2f".format(crr)}" } ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Match status
            score.status?.let {
                Text(
                    text = it,
                    color = if (it.lowercase().contains("live")) Color.Red else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}




