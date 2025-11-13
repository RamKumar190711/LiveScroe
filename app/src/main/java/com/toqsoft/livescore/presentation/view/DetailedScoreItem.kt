package com.toqsoft.livescore.presentation.score_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toqsoft.livescore.domain.model.Batter
import com.toqsoft.livescore.domain.model.Bowler
import com.toqsoft.livescore.domain.model.CricketScore
import com.toqsoft.livescore.domain.model.CricketScoreDetail
import com.toqsoft.livescore.domain.model.MatchScore
import com.toqsoft.livescore.domain.model.Score
import com.toqsoft.livescore.domain.utill.ScoreDetailTab

val CricbuzzGrey = Color(0xFFF4F4F4)
val HeaderBackground = Color(0xFFFFFFFF)
val TabIndicatorColor = Color(0xFF00B050)
val NavTextColor = Color(0xFF555555)
val DividerColor = Color(0xFFE0E0E0)


fun mapCricketScoreToDisplay(rawScore: CricketScore, isLive: Boolean): CricketScoreDetail {

    val playerMatch = if (rawScore.status.contains("won by", ignoreCase = true)) {
        if (rawScore.status.contains("Hobart Hurricanes", ignoreCase = true)) "Hayley Silver-holmes" else null
    } else null

    val currentScore = rawScore.score?.lastOrNull()
    val crr = if (isLive && currentScore != null && currentScore.o > 0) {
        currentScore.r.toDouble() / currentScore.o
    } else null

    val displayTeamScores = rawScore.teams.mapIndexedNotNull { index, teamName ->
        val score = rawScore.score?.getOrNull(index)
        if (score != null) {
            val scoreText = if (rawScore.matchType.contains("Test", ignoreCase = true) && rawScore.score.size > 1) {
                val prevScore = rawScore.score.getOrNull(index - 2)
                if (prevScore != null) "${prevScore.r} & ${score.r}/${score.w} (${score.o})"
                else "${score.r}/${score.w} (${score.o})"
            } else {
                "${score.r}/${score.w} (${score.o})"
            }

            MatchScore(
                team = teamName,
                scoreText = scoreText,
                isWinner = rawScore.status.contains(teamName, ignoreCase = true) && !isLive
            )
        } else null
    }

    return CricketScoreDetail(
        matchTitle = rawScore.name,
        matchStatus = rawScore.status,
        isLive = isLive,
        teamScores = displayTeamScores,
        inningsScores = rawScore.inningsScores ?: emptyList(),
        crr = crr,
        playerOfTheMatch = playerMatch,
        batters = rawScore.batters,
        bowlers = rawScore.bowlers,
        commentary = if (!isLive) null else listOf(
            "16.5 Maitlan Brown to Carey, FOUR, to third man",
            "16.4 Maitlan Brown to Carey, no run",
            "Maiden Brown [3.0-0-16-0] is back into the attack"
        )
    )
}


@Composable
fun DetailedScoreItem(rawScore: CricketScore) {

    val isLive = rawScore.status.lowercase().contains("live") || rawScore.status.lowercase().contains("day")

    val defaultTab = if (isLive) ScoreDetailTab.Live else ScoreDetailTab.Scorecard

    var currentTab by remember { mutableStateOf<ScoreDetailTab>(defaultTab) }

    val displayData = remember(rawScore) {
        mapCricketScoreToDisplay(rawScore, isLive)
    }

    Surface(color = HeaderBackground) {
        Column(modifier = Modifier.fillMaxSize()) {

            CricketNavigationBar(
                isLive = isLive,
                selectedTab = currentTab.title,
                onTabSelected = { newTabTitle ->
                    currentTab = ScoreDetailTab.fromTitle(newTabTitle) ?: defaultTab
                }
            )
            Divider(color = DividerColor, thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CricbuzzGrey)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = HeaderBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        when (currentTab) {
                            ScoreDetailTab.Live, ScoreDetailTab.Scorecard, ScoreDetailTab.Info -> {
                                if (displayData.isLive) {
                                    LiveMatchScoreDisplay(displayData)
                                } else {
                                    FinishedMatchScoreDisplay(displayData)
                                }

                                displayData.playerOfTheMatch?.let { player ->
                                    PlayerOfTheMatch(player)
                                } ?: run {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Divider(color = DividerColor, thickness = 1.dp)
                                }

                                if (displayData.batters != null && (currentTab == ScoreDetailTab.Scorecard || currentTab == ScoreDetailTab.Live)) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BattingScorecard(displayData.batters)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Divider(color = DividerColor, thickness = 1.dp)
                                }

                                if (displayData.bowlers != null && (currentTab == ScoreDetailTab.Scorecard || currentTab == ScoreDetailTab.Live)) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BowlingScorecard(displayData.bowlers)
                                }
                            }

                            ScoreDetailTab.FullCommentary -> {
                                displayData.commentary?.let { commentary ->
                                    CommentarySection(commentary)
                                } ?: Text("Full Commentary is not available for this match.")
                            }

                            ScoreDetailTab.Squads -> {
                                Text("Squads list placeholder.")
                            }
                            ScoreDetailTab.Overs -> {
                                Text("Overs by Over (Worm) graph placeholder.")
                            }
                            ScoreDetailTab.Highlights -> {
                                Text("Highlights video list placeholder.")
                            }
                            ScoreDetailTab.News -> {
                                Text("Related News articles placeholder.")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FinishedMatchScoreDisplay(scoreDetail: CricketScoreDetail) {
    scoreDetail.teamScores.forEach { matchScore ->
        Text(
            text = "${matchScore.team} ${matchScore.scoreText}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (matchScore.isWinner) FontWeight.Bold else FontWeight.Normal,
                color = if (matchScore.isWinner) Color.Black else Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "MATCH RESULT : ${scoreDetail.matchStatus}",
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun LiveMatchScoreDisplay(scoreDetail: CricketScoreDetail) {
    scoreDetail.inningsScores.forEach { inningsScore ->
        Text(
            text = inningsScore,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

    val scoreText = scoreDetail.teamScores.lastOrNull()?.scoreText ?: ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "${scoreDetail.teamScores.lastOrNull()?.team ?: "Team"} ${scoreText}",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontSize = 20.sp
            )
        )
        scoreDetail.crr?.let { crr ->
            Text(
                text = "CRR: ${"%.2f".format(crr)}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = scoreDetail.matchStatus,
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red, fontWeight = FontWeight.SemiBold)
    )
}

@Composable
fun CricketNavigationBar(
    isLive: Boolean,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Info", "Live", "Scorecard", "Squads", "Overs", "Highlights", "Full Commentary", "News")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .height(48.dp)
            .background(HeaderBackground)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .clickable { onTabSelected(tab) },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color.Black else NavTextColor,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    if (isSelected) {
                        Spacer(
                            modifier = Modifier.width(50.dp).height(3.dp).background(TabIndicatorColor)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerOfTheMatch(player: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Divider(color = DividerColor, thickness = 1.dp)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = "PLAYER OF THE MATCH",
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray, letterSpacing = 0.5.sp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = player,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
    Divider(color = DividerColor, thickness = 1.dp)
}

@Composable
fun BattingScorecard(batters: List<Batter>) {
    Text("Batter", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Spacer(modifier = Modifier.weight(0.40f))
        Text("R", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("B", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("4s", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("6s", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("SR", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("", modifier = Modifier.width(20.dp))
    }

    batters.forEach { batter ->
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = batter.name + if (batter.onStrike) " *" else "",
                modifier = Modifier.weight(0.40f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (batter.onStrike) FontWeight.Bold else FontWeight.Normal, color = Color.Black)
            )
            Text("${batter.runs}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (batter.onStrike) FontWeight.Bold else FontWeight.Normal))
            Text("${batter.balls}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (batter.onStrike) FontWeight.Bold else FontWeight.Normal))
            Text("${batter.fours}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (batter.onStrike) FontWeight.Bold else FontWeight.Normal))
            Text("${batter.sixes}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (batter.onStrike) FontWeight.Bold else FontWeight.Normal))
            Text("%.2f".format(batter.sr), modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (batter.onStrike) FontWeight.Bold else FontWeight.Normal))
            Text(">", modifier = Modifier.width(20.dp), textAlign = TextAlign.End, color = Color.LightGray)
        }
    }
}

@Composable
fun BowlingScorecard(bowlers: List<Bowler>) {
    Text("Bowler", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Spacer(modifier = Modifier.weight(0.40f))
        Text("O", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("M", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("R", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("W", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("ECO", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall)
        Text("", modifier = Modifier.width(20.dp))
    }

    bowlers.forEach { bowler ->
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = bowler.name + if (bowler.currentBowler) " *" else "",
                modifier = Modifier.weight(0.40f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (bowler.currentBowler) FontWeight.Bold else FontWeight.Normal, color = Color.Black)
            )
            Text("${bowler.overs}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (bowler.currentBowler) FontWeight.Bold else FontWeight.Normal))
            Text("${bowler.maidens}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (bowler.currentBowler) FontWeight.Bold else FontWeight.Normal))
            Text("${bowler.runs}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (bowler.currentBowler) FontWeight.Bold else FontWeight.Normal))
            Text("${bowler.wickets}", modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (bowler.currentBowler) FontWeight.Bold else FontWeight.Normal))
            Text("%.2f".format(bowler.eco), modifier = Modifier.weight(0.12f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (bowler.currentBowler) FontWeight.Bold else FontWeight.Normal))
            Text(">", modifier = Modifier.width(20.dp), textAlign = TextAlign.End, color = Color.LightGray)
        }
    }
}

@Composable
fun CommentarySection(commentary: List<String>) {
    Spacer(modifier = Modifier.height(8.dp))
    Text("Full Commentary", style = MaterialTheme.typography.titleMedium.copy(color = Color.Black, fontWeight = FontWeight.Bold))
    Spacer(modifier = Modifier.height(8.dp))

    commentary.forEach { line ->
        Text(line, style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray))
        Spacer(modifier = Modifier.height(4.dp))
    }
}