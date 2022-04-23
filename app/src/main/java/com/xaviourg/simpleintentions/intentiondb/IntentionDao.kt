package com.xaviourg.simpleintentions.intentiondb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IntentionDao {
    @Query("SELECT * FROM IntentionBlock ORDER BY date DESC")
    fun getAllIntentions(): Flow<MutableList<IntentionBlock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntention(intentionBlock: IntentionBlock)

    @Delete
    suspend fun deleteIntention(intentionBlock: IntentionBlock)

    @Update
    suspend fun updateIntention(intentionBlock: IntentionBlock)

    @Query("SELECT * FROM Settings ORDER BY `key` ASC")
    fun settings(): Flow<MutableList<Settings>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)
}