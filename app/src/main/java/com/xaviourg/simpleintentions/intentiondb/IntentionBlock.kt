package com.xaviourg.simpleintentions.intentiondb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class IntentionBlock(
    @PrimaryKey(autoGenerate = true) var IBID: Int? = null,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = " scope") val scope: Scope,
    @ColumnInfo(name = "intentions") val intentions: List<String>,
)