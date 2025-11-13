package com.toqsoft.livescore.domain.utill

sealed class ScoreDetailTab(val title: String) {
    data object Info : ScoreDetailTab("Info")
    data object Live : ScoreDetailTab("Live")
    data object Scorecard : ScoreDetailTab("Scorecard")
    data object Squads : ScoreDetailTab("Squads")
    data object Overs : ScoreDetailTab("Overs")
    data object Highlights : ScoreDetailTab("Highlights")
    data object FullCommentary : ScoreDetailTab("Full Commentary")
    data object News : ScoreDetailTab("News")

    companion object {
        fun fromTitle(title: String): ScoreDetailTab? {
            return when (title) {
                Info.title -> Info
                Live.title -> Live
                Scorecard.title -> Scorecard
                Squads.title -> Squads
                Overs.title -> Overs
                Highlights.title -> Highlights
                FullCommentary.title -> FullCommentary
                News.title -> News
                else -> null
            }
        }
    }
}