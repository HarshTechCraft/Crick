package com.example.atry


data class BowlingStats(
    val name: String,
    val overs: Double,
    val maidens: Int,
    val runs: Int,
    val wickets: Int,
    val economy: Double,
    val noBalls: Int,
    val wides: Int
)
