package com.example.atry

data class FlattenedData(
    val isGroupName: Boolean,
    val groupName: String? = null,
    val pointsTableInfo: PointsTableInfo? = null
)