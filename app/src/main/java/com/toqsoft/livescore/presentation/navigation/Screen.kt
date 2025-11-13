package com.toqsoft.livescore.presentation.navigation

import com.toqsoft.livescore.domain.model.CricketScore

sealed class Screen {
    object ScoreList : Screen()
    data class ScoreDetail(val score: CricketScore) : Screen()
}
