package com.thevinesh.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Displays a non-dismissable onboarding bottom sheet.
 * The only way to dismiss is by clicking the CTA button.
 *
 * @param content The content configuration (title, features, CTA)
 * @param onCtaClicked Called when the CTA button is pressed
 * @param theme Optional theme customization (defaults to Material3)
 * @param modifier Optional modifier for the sheet container
 */
@Composable
fun OnboardingSheet(
    content: OnboardingContent,
    onCtaClicked: () -> Unit,
    modifier: Modifier = Modifier,
    theme: OnboardingTheme = OnboardingTheme()
) {
    val backgroundColor = if (theme.backgroundColor == Color.Unspecified) {
        MaterialTheme.colorScheme.surface
    } else {
        theme.backgroundColor
    }
    
    val iconTint = if (theme.iconTint == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        theme.iconTint
    }

    // Full screen scrim + bottom sheet
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(
                topStart = theme.cornerRadius,
                topEnd = theme.cornerRadius
            ),
            color = backgroundColor,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(theme.contentPadding)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = content.title,
                    style = if (theme.titleStyle == androidx.compose.ui.text.TextStyle.Default) {
                        MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    } else {
                        theme.titleStyle
                    },
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Features
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    content.features.forEach { feature ->
                        FeatureRow(
                            feature = feature,
                            theme = theme,
                            iconTint = iconTint
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // CTA Button
                Button(
                    onClick = onCtaClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = theme.ctaButtonColors ?: ButtonDefaults.buttonColors(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = content.ctaText,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FeatureRow(
    feature: FeatureItem,
    theme: OnboardingTheme,
    iconTint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = feature.icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = iconTint
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = feature.title,
                style = if (theme.featureTitleStyle == androidx.compose.ui.text.TextStyle.Default) {
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                } else {
                    theme.featureTitleStyle
                },
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = feature.description,
                style = if (theme.featureDescriptionStyle == androidx.compose.ui.text.TextStyle.Default) {
                    MaterialTheme.typography.bodyMedium
                } else {
                    theme.featureDescriptionStyle
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Convenience composable that auto-shows based on state provider.
 * Automatically calls markAsShown() when CTA is clicked.
 *
 * @param stateProvider Provider for tracking shown state
 * @param content The content configuration (title, features, CTA)
 * @param onCtaClicked Called when the CTA button is pressed
 * @param theme Optional theme customization (defaults to Material3)
 */
@Composable
fun OnboardingSheetWithState(
    stateProvider: OnboardingStateProvider,
    content: OnboardingContent,
    onCtaClicked: () -> Unit,
    theme: OnboardingTheme = OnboardingTheme()
) {
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var hasChecked by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        val hasBeenShown = stateProvider.hasBeenShown()
        showSheet = !hasBeenShown
        hasChecked = true
    }
    
    if (hasChecked && showSheet) {
        OnboardingSheet(
            content = content,
            onCtaClicked = {
                scope.launch {
                    stateProvider.markAsShown()
                    showSheet = false
                    onCtaClicked()
                }
            },
            theme = theme
        )
    }
}
