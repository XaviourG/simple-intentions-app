package com.xaviourg.simpleintentions

import android.app.Application
import com.xaviourg.simpleintentions.intentiondb.IntentionDatabase
import com.xaviourg.simpleintentions.intentiondb.IntentionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { IntentionDatabase.getDatabase(this) }
    val repository by lazy { IntentionRepository(database.intentionDao())
    }

}