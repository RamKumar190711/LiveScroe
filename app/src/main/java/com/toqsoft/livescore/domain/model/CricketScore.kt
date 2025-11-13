package com.toqsoft.livescore.domain.model

// --- Domain Model (Raw API data) ---

data class CricketScore(
    val id: String,
    val name: String,
    val matchType: String,
    val status: String,
    val venue: String? = null,
    val dateTimeGMT: String? = null,
    val teams: List<String>,
    val score: List<Score>? = null,
    val flagUrls: List<String>? = null,
    val required: Required? = null,
    val batters: List<Batter>? = null,
    val bowlers: List<Bowler>? = null,
    val inningsScores: List<String>? = null // Raw data for previous innings scores
)

data class Score(
    val r: Int, // Runs
    val w: Int, // Wickets
    val o: Double // Overs
)

data class Required(
    val runs: Int,
    val balls: Int
)

data class CurrentMatchesResponse(
    val status: String,
    val data: List<CricketScore>?,
    val info: Info? = null
)

data class Info(
    val hitsToday: Int? = null,
    val hitsLimit: Int? = null,
    val credits: Int? = null,
    val totalRows: Int? = 0
)


// --- Presentation/UI Model (Transformed data for Composables) ---

data class MatchScore(
    val team: String,
    val scoreText: String, // E.g., "152/9 (20)" or "286 & 86/5 (28.5)"
    val isWinner: Boolean = false // Only relevant for finished matches
)

data class Batter(
    val name: String,
    val runs: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val sr: Double, // Strike Rate
    val onStrike: Boolean = false
)

data class Bowler(
    val name: String,
    val overs: Double,
    val maidens: Int,
    val runs: Int,
    val wickets: Int,
    val eco: Double, // Economy Rate
    val currentBowler: Boolean = false
)

data class CricketScoreDetail(
    val matchTitle: String, // E.g., "BAN vs IRE" (derived from 'name' in domain)
    val matchStatus: String, // E.g., "Ireland trail by 215 runs" (derived from 'status' in domain)
    val isLive: Boolean,
    val teamScores: List<MatchScore>, // Transformed scores
    val inningsScores: List<String> = emptyList(),
    val crr: Double? = null,
    val playerOfTheMatch: String? = null,
    val batters: List<Batter>? = null, // Can be null if data is missing
    val bowlers: List<Bowler>? = null, // Can be null if data is missing
    val commentary: List<String>? = null // Can be null
)