package com.toqsoft.livescore.presentation.viewmodel

import com.toqsoft.livescore.domain.model.CricketScore

sealed class UiState {
    object Loading : UiState()
    data class Success(val scores: List<CricketScore>) : UiState()
    data class Error(val message: String) : UiState()
}
