package com.example.atry


data class PointsTableInfo(
    val nrr: String,
    val teamFullName: String,
    val teamId: Int,
    val teamImageId: Int,
    val teamName: String,
    val matchesPlayed: Int,
    val matchesWon: Int,
    val matchesLost: Int,
    val points: Int,
    val noResult: Int
)
