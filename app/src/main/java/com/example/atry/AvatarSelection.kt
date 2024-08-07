package com.example.atry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AvatarSelection : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    private var selectedAvatarResId: Int = R.drawable.avatar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_selection)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val selectedAvatarImageView = findViewById<ImageView>(R.id.selected_avatar)
        val sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        selectedAvatarResId = sharedPref.getInt("selectedAvatarResId", R.drawable.avatar)
        selectedAvatarImageView.setImageResource(selectedAvatarResId)

        selectedAvatarImageView.setImageResource(selectedAvatarResId)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@AvatarSelection, R.drawable.ic_custom_back))
            setDisplayShowTitleEnabled(false)
        }




        val avatarClickListener = View.OnClickListener { view ->
            selectedAvatarResId = when (view.id) {
                R.id.avatar1 -> R.drawable.avatar
                R.id.avatar2 -> R.drawable.avatar2
                R.id.avatar3 -> R.drawable.avatar3
                R.id.avatar4 -> R.drawable.avatar4
                R.id.avatar5 -> R.drawable.avatar5
                R.id.avatar6 -> R.drawable.avatar6
                R.id.avatar7 -> R.drawable.avatar7
                R.id.avatar8 -> R.drawable.avatar8
                R.id.avatar9 -> R.drawable.avatar9
                R.id.avatar10 -> R.drawable.avatar10
                R.id.avatar11 -> R.drawable.avatar11
                R.id.avatar12 -> R.drawable.avatar12
                else -> R.drawable.avatar
            }
            selectedAvatarImageView.setImageResource(selectedAvatarResId)
        }

        findViewById<ImageView>(R.id.avatar1).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar2).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar3).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar4).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar5).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar6).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar7).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar8).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar9).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar10).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar11).setOnClickListener(avatarClickListener)
        findViewById<ImageView>(R.id.avatar12).setOnClickListener(avatarClickListener)

        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        findViewById<Button>(R.id.save_button).setOnClickListener {

            val sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("selectedAvatarResId", selectedAvatarResId)
                apply()
            }

            setResult(RESULT_OK)
            finish()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}