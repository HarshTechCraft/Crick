package com.example.atry

data class DataSquad(
    val isTeamName: Boolean,
    val teamName: String? = null,
    val team1Img: String? = null,
    val team2Img: String? = null,
    val teamInfo: List<teamInfo>? = null
)
