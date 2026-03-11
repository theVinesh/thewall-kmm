package com.thevinesh.thewall.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

enum class TheWallSampleStage {
    OnboardingWall,
    Welcome,
    CloseableWall,
}

internal enum class TheWallSampleAction {
    CompleteOnboarding,
    ShowCloseableWall,
    DismissCloseableWall,
}

@Composable
fun rememberTheWallSampleState(
    initialStage: TheWallSampleStage = TheWallSampleStage.OnboardingWall,
): TheWallSampleState = remember(initialStage) { TheWallSampleState(initialStage) }

@Stable
class TheWallSampleState internal constructor(initialStage: TheWallSampleStage) {
    var stage by mutableStateOf(initialStage)
        private set

    fun completeOnboarding() {
        dispatch(TheWallSampleAction.CompleteOnboarding)
    }

    fun showCloseableWall() {
        dispatch(TheWallSampleAction.ShowCloseableWall)
    }

    fun dismissCloseableWall() {
        dispatch(TheWallSampleAction.DismissCloseableWall)
    }

    internal fun dispatch(action: TheWallSampleAction) {
        stage = stage.reduce(action)
    }
}

internal fun TheWallSampleStage.reduce(action: TheWallSampleAction): TheWallSampleStage = when (this) {
    TheWallSampleStage.OnboardingWall -> when (action) {
        TheWallSampleAction.CompleteOnboarding -> TheWallSampleStage.Welcome
        else -> this
    }

    TheWallSampleStage.Welcome -> when (action) {
        TheWallSampleAction.ShowCloseableWall -> TheWallSampleStage.CloseableWall
        else -> this
    }

    TheWallSampleStage.CloseableWall -> when (action) {
        TheWallSampleAction.DismissCloseableWall -> TheWallSampleStage.Welcome
        else -> this
    }
}