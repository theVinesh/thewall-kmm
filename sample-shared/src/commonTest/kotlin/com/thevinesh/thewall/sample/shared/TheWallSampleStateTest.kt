package com.thevinesh.thewall.sample.shared

import kotlin.test.Test
import kotlin.test.assertEquals

class TheWallSampleStateTest {

    @Test
    fun `sample flow matches android demo behavior`() {
        var stage = TheWallSampleStage.OnboardingWall

        stage = stage.reduce(TheWallSampleAction.CompleteOnboarding)
        assertEquals(TheWallSampleStage.Welcome, stage)

        stage = stage.reduce(TheWallSampleAction.ShowCloseableWall)
        assertEquals(TheWallSampleStage.CloseableWall, stage)

        stage = stage.reduce(TheWallSampleAction.DismissCloseableWall)
        assertEquals(TheWallSampleStage.Welcome, stage)
    }

    @Test
    fun `unsupported actions keep the current stage`() {
        assertEquals(
            TheWallSampleStage.OnboardingWall,
            TheWallSampleStage.OnboardingWall.reduce(TheWallSampleAction.DismissCloseableWall),
        )
        assertEquals(
            TheWallSampleStage.Welcome,
            TheWallSampleStage.Welcome.reduce(TheWallSampleAction.CompleteOnboarding),
        )
        assertEquals(
            TheWallSampleStage.CloseableWall,
            TheWallSampleStage.CloseableWall.reduce(TheWallSampleAction.ShowCloseableWall),
        )
    }
}