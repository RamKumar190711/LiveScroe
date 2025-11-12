package com.toqsoft.livescore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toqsoft.livescore.data.repository.CricketRepository
import com.toqsoft.livescore.domain.model.CricketScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val repository: CricketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val apiKey = "6c5a8fad-5dac-4c99-85aa-04ae9234f706"

    init {
        fetchLiveMatches()
    }

    private fun fetchLiveMatches() {
        viewModelScope.launch {
            try {
                val matches = repository.getLiveMatches(apiKey).body()
                if (!matches.isNullOrEmpty()) {
                    _uiState.value = UiState.Success(matches)
                } else {
                    _uiState.value = UiState.Error("No live matches found or invalid API key")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
