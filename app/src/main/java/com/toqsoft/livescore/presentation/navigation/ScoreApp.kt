package com.toqsoft.livescore.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.toqsoft.livescore.presentation.score_detail.DetailedScoreItem
import com.toqsoft.livescore.presentation.view.ScoreScreen
import com.toqsoft.livescore.presentation.viewmodel.ScoreViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScoreApp(viewModel: ScoreViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.ScoreList) }

    when (val screen = currentScreen) {
        is Screen.ScoreList -> {
            ScoreScreen(viewModel = viewModel) { selectedScore ->
                currentScreen = Screen.ScoreDetail(selectedScore)
            }
        }
        is Screen.ScoreDetail -> {
            DetailedScoreItem(rawScore = screen.score)
        }
    }
}
