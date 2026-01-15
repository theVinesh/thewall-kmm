package com.thevinesh.thewall

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TheWallThemeTest {
    
    @Test
    fun `default theme uses unspecified colors`() {
        val theme = TheWallTheme()
        
        assertEquals(Color.Unspecified, theme.backgroundColor)
        assertEquals(Color.Unspecified, theme.iconTint)
    }
    
    @Test
    fun `default theme uses default text styles`() {
        val theme = TheWallTheme()
        
        assertEquals(TextStyle.Default, theme.titleStyle)
        assertEquals(TextStyle.Default, theme.featureTitleStyle)
        assertEquals(TextStyle.Default, theme.featureDescriptionStyle)
    }
    
    @Test
    fun `default theme has null button colors`() {
        val theme = TheWallTheme()
        
        assertNull(theme.ctaButtonColors)
    }
    
    @Test
    fun `default theme has 28dp corner radius`() {
        val theme = TheWallTheme()
        
        assertEquals(28.dp, theme.cornerRadius)
    }
    
    @Test
    fun `default theme has 24dp content padding`() {
        val theme = TheWallTheme()
        
        assertEquals(PaddingValues(24.dp), theme.contentPadding)
    }
    
    @Test
    fun `custom theme values are preserved`() {
        val customColor = Color.Red
        val customRadius = 16.dp
        
        val theme = TheWallTheme(
            backgroundColor = customColor,
            cornerRadius = customRadius
        )
        
        assertEquals(customColor, theme.backgroundColor)
        assertEquals(customRadius, theme.cornerRadius)
    }
}
