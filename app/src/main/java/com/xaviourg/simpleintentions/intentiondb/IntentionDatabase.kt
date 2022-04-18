package com.xaviourg.simpleintentions.intentiondb

import android.content.Context
import android.os.Build
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(entities = [IntentionBlock::class], views= [DailyIntentionView::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class IntentionDatabase: RoomDatabase() {
    abstract fun intentionDao(): IntentionDao

    companion object { // singleton functionality for database
        @Volatile
        private var INSTANCE : IntentionDatabase? = null
        fun getDatabase(context: Context):IntentionDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IntentionDatabase::class.java,
                    "intention_database"
                ).addCallback(IntentionCallback())
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    //If no database exists, initialise one
    private class IntentionCallback(): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }
}