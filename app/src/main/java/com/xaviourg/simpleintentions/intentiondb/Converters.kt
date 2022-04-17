package com.xaviourg.simpleintentions.intentiondb

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toScope(value: Int): Scope {
        return when(value){
            1 -> Scope.DAILY
            2 -> Scope.WEEKLY
            3 -> Scope.FORTNIGHTLY
            4 -> Scope.MONTHLY
            5 -> Scope.QUARTERLY
            6 -> Scope.BIYEARLY
            7 -> Scope.YEARLY
            8 -> Scope.BIDECENNIAL
            9 -> Scope.DECENNIAL
            else -> Scope.LIFE
        }
    }
    @TypeConverter
    fun fromScope(scope: Scope): Int {
        return scope.ordinal
    }

    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split("|||").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = "|||")
    }
}