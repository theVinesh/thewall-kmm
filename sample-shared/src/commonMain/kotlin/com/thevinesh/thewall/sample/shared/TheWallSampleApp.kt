package com.thevinesh.thewall.sample.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thevinesh.thewall.FeatureItem
import com.thevinesh.thewall.TheWallContent
import com.thevinesh.thewall.TheWallSheet

@Composable
fun TheWallSampleApp(
    modifier: Modifier = Modifier,
    state: TheWallSampleState = rememberTheWallSampleState(),
) {
    MaterialTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.stage == TheWallSampleStage.OnboardingWall) {
                        Text(
                            text = "TheWall shown...",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    } else {
                        WelcomeContent(onShowCloseableWall = state::showCloseableWall)
                    }
                }

                if (state.stage == TheWallSampleStage.OnboardingWall) {
                    TheWallSheet(
                        content = onboardingWallContent,
                        onCtaClicked = state::completeOnboarding,
                    )
                }

                if (state.stage == TheWallSampleStage.CloseableWall) {
                    TheWallSheet(
                        content = closeableWallContent,
                        onCtaClicked = state::dismissCloseableWall,
                        onClose = state::dismissCloseableWall,
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeContent(onShowCloseableWall: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Welcome! 🎉",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onShowCloseableWall) {
            Text("Demo Wall with Close Button")
        }
    }
}

private val onboardingWallContent = TheWallContent(
    title = "Welcome to the App",
    features = listOf(
        FeatureItem(
            icon = Icons.Default.Widgets,
            title = "Beautiful Widgets",
            description = "Track your stats right from your home screen",
        ),
        FeatureItem(
            icon = Icons.Default.Refresh,
            title = "Auto Updates",
            description = "Your data stays fresh with automatic syncing",
        ),
        FeatureItem(
            icon = Icons.Default.Notifications,
            title = "Smart Alerts",
            description = "Get notified about important changes",
        ),
    ),
    ctaText = "Get Started",
)

private val closeableWallContent = TheWallContent(
    title = "Closeable Wall Demo",
    features = listOf(
        FeatureItem(
            icon = Icons.Default.Widgets,
            title = "Optional Close",
            description = "This wall can be dismissed using the close button in the top-right",
        ),
        FeatureItem(
            icon = Icons.Default.Refresh,
            title = "Or Use CTA",
            description = "You can still use the CTA button to proceed",
        ),
    ),
    ctaText = "Continue",
)