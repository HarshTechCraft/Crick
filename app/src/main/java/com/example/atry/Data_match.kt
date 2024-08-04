package com.example.atry

data class Data_match(
    val matchId: String,
    val team1: String,
    val team2: String,
    val matchType: String,
    val date: String,
    val matchNumber: Int,
    val venue: String,
    val status: String,
    val team1Short: String,
    val team2Short: String,
    val seriesId: String,
    val state: String,
    val team1Score: String,
    val team2Score: String,
    val team1Over: Double,
    val team2Over: Double
)
