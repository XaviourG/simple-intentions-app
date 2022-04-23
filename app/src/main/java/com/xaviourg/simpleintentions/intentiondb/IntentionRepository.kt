package com.xaviourg.simpleintentions.intentiondb

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class IntentionRepository(private val intentionDao: IntentionDao) {

    val allIntentions: Flow<MutableList<IntentionBlock>> = intentionDao.getAllIntentions()
    val settings: Flow<MutableList<Settings>> = intentionDao.settings()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertIntention(intentionBlock: IntentionBlock){
        println("INSERTING <<$intentionBlock>> INTO DATABASE")
        intentionDao.insertIntention(intentionBlock)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteIntention(intentionBlock: IntentionBlock){
        intentionDao.deleteIntention(intentionBlock)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateIntention(intentionBlock: IntentionBlock) {
        intentionDao.updateIntention(intentionBlock)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSettings(settings: Settings) {
        intentionDao.insertSettings(settings)
    }

}