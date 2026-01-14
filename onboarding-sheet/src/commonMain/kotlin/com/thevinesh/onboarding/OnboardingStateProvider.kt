package com.thevinesh.onboarding

/**
 * Interface for tracking onboarding shown state.
 *
 * Host app implements this with their preferred storage mechanism
 * (DataStore, UserDefaults, SharedPreferences, etc.)
 *
 * Example implementation:
 * ```kotlin
 * class MyOnboardingState(private val dataStore: DataStore<Preferences>) : OnboardingStateProvider {
 *     override suspend fun hasBeenShown(): Boolean = dataStore.data.first()[ONBOARDING_SHOWN] == true
 *     override suspend fun markAsShown() { dataStore.edit { it[ONBOARDING_SHOWN] = true } }
 * }
 * ```
 */
interface OnboardingStateProvider {
    /**
     * Returns true if onboarding has already been shown to the user.
     */
    suspend fun hasBeenShown(): Boolean
    
    /**
     * Marks onboarding as shown. Called automatically when CTA is clicked.
     */
    suspend fun markAsShown()
}
