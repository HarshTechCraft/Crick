package com.example.atry

import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FeedBack : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)

        val feedback_cancel = findViewById<Button>(R.id.feedback_cancel)
        val feedback_submit = findViewById<Button>(R.id.feedback_submit)

        feedback_submit.setOnClickListener {
            onBackPressed()
        }
        feedback_cancel.setOnClickListener {
            onBackPressed()
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@FeedBack, R.drawable.ic_custom_back))
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}