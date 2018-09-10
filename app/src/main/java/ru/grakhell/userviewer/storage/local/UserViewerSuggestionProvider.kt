package ru.grakhell.userviewer.storage.local

import android.content.SearchRecentSuggestionsProvider

class UserViewerSuggestionProvider: SearchRecentSuggestionsProvider() {
    companion object {
        const val AUTHORITY:String = "ru.grakhell.userviewer.storage.local.UserViewerSuggestionProvider"
        const val MODE:Int = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}