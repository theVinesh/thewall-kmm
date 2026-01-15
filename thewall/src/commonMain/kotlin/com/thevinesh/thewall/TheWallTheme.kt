package com.thevinesh.thewall

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Theme configuration for TheWall sheet.
 * All properties have sensible Material3 defaults.
 *
 * Pass [Color.Unspecified] or [TextStyle.Default] to use theme defaults.
 */
data class TheWallTheme(
    /**
     * Background color of the sheet. Uses Material3 surface by default.
     */
    val backgroundColor: Color = Color.Unspecified,
    
    /**
     * Text style for the main title.
     */
    val titleStyle: TextStyle = TextStyle.Default,
    
    /**
     * Text style for feature titles.
     */
    val featureTitleStyle: TextStyle = TextStyle.Default,
    
    /**
     * Text style for feature descriptions.
     */
    val featureDescriptionStyle: TextStyle = TextStyle.Default,
    
    /**
     * Colors for the CTA button. Uses Material3 filled button colors by default.
     */
    val ctaButtonColors: ButtonColors? = null,
    
    /**
     * Tint color for feature icons. Uses Material3 primary by default.
     */
    val iconTint: Color = Color.Unspecified,
    
    /**
     * Corner radius for the bottom sheet.
     */
    val cornerRadius: Dp = 28.dp,
    
    /**
     * Padding around the sheet content.
     */
    val contentPadding: PaddingValues = PaddingValues(24.dp)
)
