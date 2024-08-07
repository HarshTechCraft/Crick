package com.example.atry

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Match : AppCompatActivity() {
    lateinit var toolbar: Toolbar


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        val matchId = intent.getStringExtra("matchId").orEmpty()
        val team1img = intent.getStringExtra("team1img").orEmpty()
        val team2img = intent.getStringExtra("team2img").orEmpty()
        val date = intent.getStringExtra("date").orEmpty()
        val status = intent.getStringExtra(NotificationCompat.CATEGORY_STATUS).orEmpty()
        val state = intent.getStringExtra("state").orEmpty()
        val team1Long = intent.getStringExtra("team1").orEmpty()
        val team2Long = intent.getStringExtra("team2").orEmpty()
        val seriesId = intent.getStringExtra("seriesId").orEmpty()
        val team1Short = intent.getStringExtra("team1Short").orEmpty()
        val team2Short = intent.getStringExtra("team2Short").orEmpty()
        val team1Score = intent.getStringExtra("team1Score").orEmpty()
        val team2Score = intent.getStringExtra("team2Score").orEmpty()
        val team1Over = intent.getDoubleExtra("team1Over", 0.0)
        val team2Over = intent.getDoubleExtra("team2Over", 0.0)
        val matchType = intent.getStringExtra("matchType").orEmpty()


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = Tab_adapter_match(
            this, matchId, team1Short, team2Short, team1img, team2img, date, status,
            team1Long, team2Long, seriesId, team1Score, team2Score, team1Over, team2Over, state , matchType
        )

        val containsTour = matchType.contains("tour", ignoreCase = true)
        Log.d("table","$containsTour $matchId")

        if(!containsTour) {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Live"
                    1 -> tab.text = "Scorecard"
                    2 -> tab.text = "Point Table"
                    3 -> tab.text = "Key Moments"
                    4 -> tab.text = "Squad"
                    else -> tab.text = "Commentary"
                }
            }.attach()
        }
        else{
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Live"
                    1 -> tab.text = "Scorecard"
                    2 -> tab.text = "Key Moments"
                    3 -> tab.text = "Squad"
                    else -> tab.text = "Commentary"
                }
            }.attach()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@Match, R.drawable.ic_custom_back))
            setDisplayShowTitleEnabled(false)
        }

        toolbarTitle.text = "CrickLive"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
