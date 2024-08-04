package com.example.atry

import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class Tab_adapter_match(
    fa: FragmentActivity,
    private val matchId: String,
    private val team1Short: String,
    private val team2Short: String,
    private val team1Img: String,
    private val team2Img: String,
    private val date: String,
    private val status: String,
    private val team1Long: String,
    private val team2Long: String,
    private val seriesId: String,
    private val team1Score: String,
    private val team2Score: String,
    private val team1Over: Double,
    private val team2Over: Double,
    private val state: String,
    private val matchType: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return if (matchType.contains("tour", ignoreCase = true)) {
            4
        } else {
            5
        }
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = if (matchType.contains("tour", ignoreCase = true)) {
            when (position) {
                0 -> Live()
                1 -> Scorecard()
                2 -> KeyMoments()
                else -> Squad()
            }
        } else {
            when (position) {
                0 -> Live()
                1 -> Scorecard()
                2 -> PointTable()
                3 -> KeyMoments()
                else -> Squad()
            }
        }

        fragment.arguments = Bundle().apply {
            putString("matchId", matchId)
            putString("team1", team1Short)
            putString("team2", team2Short)
            putString(NotificationCompat.CATEGORY_STATUS, status)
            putString("team1img", team1Img)
            putString("team2img", team2Img)
            putString("date", date)
            putString("team1Long", team1Long)
            putString("team2Long", team2Long)
            putString("seriesId", seriesId)
            putString("team1Score", team1Score)
            putString("team2Score", team2Score)
            putDouble("team1Over", team1Over)
            putDouble("team2Over", team2Over)
            putString("state", state)
        }
        return fragment
    }
}
