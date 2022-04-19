package com.xaviourg.simpleintentions.intentiondb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey(autoGenerate = false) var key: Int,
    @ColumnInfo(name = "intentionCount") val intentionCount: Int, // Must be between 1 and 5
    @ColumnInfo(name = "theme") val theme: String,
    @ColumnInfo(name = "mainBlockScope") val mainBlockScope: Scope,
    @ColumnInfo(name = "subLeftBlockScope") val subLeftBlockScope: Scope,
    @ColumnInfo(name = "subRightBlockScope") val subRightBlockScope: Scope,
)