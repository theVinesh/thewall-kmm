package com.thevinesh.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class OnboardingContentTest {
    
    @Test
    fun `content with valid data is created successfully`() {
        val content = OnboardingContent(
            title = "Welcome",
            features = listOf(
                FeatureItem(
                    icon = Icons.Default.Star,
                    title = "Feature 1",
                    description = "Description 1"
                )
            ),
            ctaText = "Get Started"
        )
        
        assertEquals("Welcome", content.title)
        assertEquals(1, content.features.size)
        assertEquals("Get Started", content.ctaText)
    }
    
    @Test
    fun `content with empty features list is valid`() {
        val content = OnboardingContent(
            title = "Welcome",
            features = emptyList(),
            ctaText = "Get Started"
        )
        
        assertTrue(content.features.isEmpty())
    }
    
    @Test
    fun `content with blank title throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            OnboardingContent(
                title = "   ",
                features = emptyList(),
                ctaText = "Get Started"
            )
        }
    }
    
    @Test
    fun `content with blank CTA text throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            OnboardingContent(
                title = "Welcome",
                features = emptyList(),
                ctaText = ""
            )
        }
    }
    
    @Test
    fun `feature item with blank title throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            FeatureItem(
                icon = Icons.Default.Star,
                title = "",
                description = "Some description"
            )
        }
    }
    
    @Test
    fun `feature item with empty description is valid`() {
        val feature = FeatureItem(
            icon = Icons.Default.Star,
            title = "Feature",
            description = ""
        )
        
        assertEquals("", feature.description)
    }
}
