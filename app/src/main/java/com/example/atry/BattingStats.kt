package com.example.atry


data class BattingStats(
    val name: String,
    val runs: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val strikeRate: Double,
    val dismissalText: String
)
