package com.example.atry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AboutUs : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@AboutUs, R.drawable.ic_custom_back))
            setDisplayShowTitleEnabled(false)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}