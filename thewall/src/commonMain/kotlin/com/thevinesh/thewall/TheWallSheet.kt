package com.thevinesh.thewall

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private enum class TheWallDismissAction {
    Cta,
    Close,
}

/**
 * Displays a TheWall bottom sheet.
 * Can be dismissed by clicking the CTA button, or optionally via close button.
 *
 * @param content The content configuration (title, features, CTA)
 * @param onCtaClicked Called when the CTA button is pressed
 * @param onClose Optional callback for close button. If provided, renders close button in top-right.
 * @param theme Optional theme customization (defaults to Material3)
 * @param modifier Optional modifier for the sheet container
 */
@Composable
fun TheWallSheet(
    content: TheWallContent,
    onCtaClicked: () -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    theme: TheWallTheme = TheWallTheme()
) {
    val onCtaClickedState by rememberUpdatedState(onCtaClicked)
    val onCloseState by rememberUpdatedState(onClose)
    val visibilityState = remember { MutableTransitionState(false) }
    var pendingDismissAction by remember { mutableStateOf<TheWallDismissAction?>(null) }

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

    val scrimAlpha by animateFloatAsState(
        targetValue = if (visibilityState.targetState) 0.5f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "thewall-scrim-alpha"
    )

    LaunchedEffect(Unit) {
        visibilityState.targetState = true
    }

    LaunchedEffect(visibilityState.isIdle, visibilityState.currentState, pendingDismissAction) {
        if (!visibilityState.isIdle || visibilityState.currentState) {
            return@LaunchedEffect
        }

        when (pendingDismissAction) {
            TheWallDismissAction.Cta -> onCtaClickedState()
            TheWallDismissAction.Close -> onCloseState?.invoke()
            null -> Unit
        }
        pendingDismissAction = null
    }

    fun startDismiss(action: TheWallDismissAction) {
        if (pendingDismissAction != null) return
        pendingDismissAction = action
        visibilityState.targetState = false
    }

    val shouldRenderOverlay =
        visibilityState.currentState || visibilityState.targetState || pendingDismissAction != null

    // Full screen scrim + bottom sheet
    if (shouldRenderOverlay) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Consume clicks on scrim - do nothing */ }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visibleState = visibilityState,
                modifier = modifier.align(Alignment.BottomCenter),
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 280),
                    initialOffsetY = { fullHeight -> fullHeight }
                ) + fadeIn(animationSpec = tween(durationMillis = 220)),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 240),
                    targetOffsetY = { fullHeight -> fullHeight }
                ) + fadeOut(animationSpec = tween(durationMillis = 180))
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = theme.cornerRadius,
                        topEnd = theme.cornerRadius
                    ),
                    color = backgroundColor,
                    tonalElevation = 2.dp
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
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
                                onClick = { startDismiss(TheWallDismissAction.Cta) },
                                enabled = pendingDismissAction == null,
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

                        // Close button - positioned in top-right corner
                        if (onClose != null) {
                            IconButton(
                                onClick = { startDismiss(TheWallDismissAction.Close) },
                                enabled = pendingDismissAction == null,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(
    feature: FeatureItem,
    theme: TheWallTheme,
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
fun TheWallSheetWithState(
    stateProvider: TheWallStateProvider,
    content: TheWallContent,
    onCtaClicked: () -> Unit,
    theme: TheWallTheme = TheWallTheme()
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
        TheWallSheet(
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
