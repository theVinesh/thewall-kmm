package com.thevinesh.thewall

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Content configuration for TheWall bottom sheet.
 *
 * @property title The main title displayed at the top of the sheet
 * @property features List of feature items to display (recommended: 2-3 items)
 * @property ctaText Text for the call-to-action button
 */
data class TheWallContent(
    val title: String,
    val features: List<FeatureItem>,
    val ctaText: String
) {
    init {
        require(title.isNotBlank()) { "Title must not be blank" }
        require(ctaText.isNotBlank()) { "CTA text must not be blank" }
    }
}

/**
 * A single feature item displayed in TheWall sheet.
 *
 * @property icon The icon to display (Material icon or custom ImageVector)
 * @property title Short title for the feature
 * @property description Longer description of the feature
 */
data class FeatureItem(
    val icon: ImageVector,
    val title: String,
    val description: String
) {
    init {
        require(title.isNotBlank()) { "Feature title must not be blank" }
    }
}
