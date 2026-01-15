package com.thevinesh.thewall

/**
 * Interface for tracking TheWall shown state.
 *
 * Host app implements this with their preferred storage mechanism
 * (DataStore, UserDefaults, SharedPreferences, etc.)
 *
 * Example implementation:
 * ```kotlin
 * class MyTheWallState(private val dataStore: DataStore<Preferences>) : TheWallStateProvider {
 *     override suspend fun hasBeenShown(): Boolean = dataStore.data.first()[ONBOARDING_SHOWN] == true
 *     override suspend fun markAsShown() { dataStore.edit { it[ONBOARDING_SHOWN] = true } }
 * }
 * ```
 */
interface TheWallStateProvider {
    /**
     * Returns true if TheWall has already been shown to the user.
     */
    suspend fun hasBeenShown(): Boolean
    
    /**
     * Marks TheWall as shown. Called automatically when CTA is clicked.
     */
    suspend fun markAsShown()
}
