package com.toqsoft.livescore.domain.model

data class CricketScore(
    val id: String,
    val name: String,
    val matchType: String,
    val status: String,
    val venue: String? = null,
    val dateTimeGMT: String? = null,
    val teams: List<String>,
    val score: List<Score>? = null,
    val flagUrls: List<String>? = null,   // Add this for team flags
    val required: Required? = null        // Optional, for runs/balls needed
)

data class Score(
    val r: Int,
    val w: Int,
    val o: Double,
    val inning: String
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
