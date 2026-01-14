package com.thevinesh.onboarding.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thevinesh.onboarding.FeatureItem
import com.thevinesh.onboarding.OnboardingContent
import com.thevinesh.onboarding.OnboardingSheet
import com.thevinesh.onboarding.OnboardingSheetWithState
import com.thevinesh.onboarding.OnboardingStateProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OnboardingSampleApp()
                }
            }
        }
    }
}

@Composable
fun OnboardingSampleApp() {
    var showOnboarding by remember { mutableStateOf(true) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content (shown behind the onboarding sheet)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (showOnboarding) "Onboarding shown..." else "Welcome! 🎉",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        // Onboarding sheet overlay
        if (showOnboarding) {
            OnboardingSheet(
                content = OnboardingContent(
                    title = "Welcome to the App",
                    features = listOf(
                        FeatureItem(
                            icon = Icons.Default.Widgets,
                            title = "Beautiful Widgets",
                            description = "Track your stats right from your home screen"
                        ),
                        FeatureItem(
                            icon = Icons.Default.Refresh,
                            title = "Auto Updates",
                            description = "Your data stays fresh with automatic syncing"
                        ),
                        FeatureItem(
                            icon = Icons.Default.Notifications,
                            title = "Smart Alerts",
                            description = "Get notified about important changes"
                        )
                    ),
                    ctaText = "Get Started"
                ),
                onCtaClicked = {
                    showOnboarding = false
                }
            )
        }
    }
}

/**
 * Example implementation of OnboardingStateProvider using in-memory storage.
 * In a real app, you would use DataStore, SharedPreferences, etc.
 */
class InMemoryOnboardingState : OnboardingStateProvider {
    private var shown = false
    
    override suspend fun hasBeenShown(): Boolean = shown
    
    override suspend fun markAsShown() {
        shown = true
    }
}
