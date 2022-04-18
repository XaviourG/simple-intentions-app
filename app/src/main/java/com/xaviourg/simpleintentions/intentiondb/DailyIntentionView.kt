package com.xaviourg.simpleintentions.intentiondb

import androidx.room.DatabaseView

@DatabaseView("SELECT intentions FROM IntentionBlock")
data class DailyIntentionView(
    val intentions: List<String>
)