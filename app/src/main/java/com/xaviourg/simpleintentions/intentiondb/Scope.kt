package com.xaviourg.simpleintentions.intentiondb

enum class Scope(val value: Int) {
    DAILY(1),
    WEEKLY(2),
    FORTNIGHTLY(3),
    MONTHLY(4),
    QUARTERLY(5),
    BIYEARLY(6),
    YEARLY(7),
    BIDECENNIAL(8),
    DECENNIAL(9),
    LIFE(10)
}