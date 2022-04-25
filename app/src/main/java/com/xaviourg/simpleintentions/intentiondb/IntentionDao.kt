package com.xaviourg.simpleintentions.intentiondb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IntentionDao {
    @Query("SELECT * FROM IntentionBlock ORDER BY date DESC")
    fun getAllIntentions(): Flow<MutableList<IntentionBlock>>

    @Query("SELECT * FROM IntentionBlock WHERE ` scope` == :givenScope ORDER BY date DESC")
    fun getIntentionsOfScope(givenScope: Scope): List<IntentionBlock>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntention(intentionBlock: IntentionBlock)

    @Delete
    suspend fun deleteIntention(intentionBlock: IntentionBlock)

    @Update
    suspend fun updateIntention(intentionBlock: IntentionBlock)

    @Query("SELECT * FROM Settings ORDER BY `key` ASC")
    fun settings(): Flow<MutableList<Settings>>

    @Query("SELECT * FROM Settings WHERE `key` IS 1")
    fun getSettings(): List<Settings>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)
}