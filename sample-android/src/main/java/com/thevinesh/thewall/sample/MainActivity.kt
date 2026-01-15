package com.thevinesh.thewall.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.thevinesh.thewall.FeatureItem
import com.thevinesh.thewall.TheWallContent
import com.thevinesh.thewall.TheWallSheet
import com.thevinesh.thewall.TheWallStateProvider

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
                    TheWallSampleApp()
                }
            }
        }
    }
}

@Composable
fun TheWallSampleApp() {
    var showTheWall by remember { mutableStateOf(true) }
    var showCloseableWall by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content (shown behind TheWall sheet)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (showTheWall) {
                Text(
                    text = "TheWall shown...",
                    style = MaterialTheme.typography.headlineMedium
                )
            } else {
                // Welcome screen with demo button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome! 🎉",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = { showCloseableWall = true }) {
                        Text("Demo Wall with Close Button")
                    }
                }
            }
        }

        // TheWall sheet overlay (onboarding)
        if (showTheWall) {
            TheWallSheet(
                content = TheWallContent(
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
                    showTheWall = false
                }
            )
        }

        // TheWall sheet with close button (demo)
        if (showCloseableWall) {
            TheWallSheet(
                content = TheWallContent(
                    title = "Closeable Wall Demo",
                    features = listOf(
                        FeatureItem(
                            icon = Icons.Default.Widgets,
                            title = "Optional Close",
                            description = "This wall can be dismissed using the close button in the top-right"
                        ),
                        FeatureItem(
                            icon = Icons.Default.Refresh,
                            title = "Or Use CTA",
                            description = "You can still use the CTA button to proceed"
                        )
                    ),
                    ctaText = "Continue"
                ),
                onCtaClicked = {
                    showCloseableWall = false
                },
                onClose = {
                    showCloseableWall = false
                }
            )
        }
    }
}

/**
 * Example implementation of TheWallStateProvider using in-memory storage.
 * In a real app, you would use DataStore, SharedPreferences, etc.
 */
class InMemoryTheWallState : TheWallStateProvider {
    private var shown = false
    
    override suspend fun hasBeenShown(): Boolean = shown
    
    override suspend fun markAsShown() {
        shown = true
    }
}
