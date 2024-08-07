package com.example.atry

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var blurView: BlurView
    private val REQUEST_CODE_AVATAR_SELECTION = 1
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {



        val sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val themeMode = sharedPref.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(themeMode)




        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerSetUp()
        tabLayoutSetUp()
        themeToggle()

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "CrickLive"

        val navItemRateUs = findViewById<ImageView>(R.id.nav_item_rate_us)
        navItemRateUs.setOnClickListener {
            val navLinearLayout: LinearLayout = findViewById(R.id.nav_linear_layout)
            selectNavItem(navItemRateUs, navLinearLayout)
            showRateUsDialog()
        }

        val aboutus  = findViewById<ImageView>(R.id.aboutUs)
        aboutus.setOnClickListener{
            val navLinearLayout: LinearLayout = findViewById(R.id.nav_linear_layout)
            selectNavItem(aboutus, navLinearLayout)
            val intent = Intent(this, AboutUs::class.java)
            startActivity(intent)
        }
        val feedback  = findViewById<ImageView>(R.id.feedBack)
        feedback.setOnClickListener{
            val navLinearLayout: LinearLayout = findViewById(R.id.nav_linear_layout)
            selectNavItem(feedback, navLinearLayout)
                    val intent = Intent(this, FeedBack::class.java)
                    startActivity(intent)
        }

        val home  = findViewById<ImageView>(R.id.home)
        home.setOnClickListener{
            val navLinearLayout: LinearLayout = findViewById(R.id.nav_linear_layout)
            selectNavItem(home, navLinearLayout)
            closeDrawer()
        }




        val profile = findViewById<ImageView>(R.id.profile_image)
        profile.setOnClickListener {
            val intent = Intent(this, AvatarSelection::class.java)
            startActivityForResult(intent, REQUEST_CODE_AVATAR_SELECTION)
        }

        updateProfileImage()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_AVATAR_SELECTION && resultCode == RESULT_OK) {
            updateProfileImage()
        }
    }

    private fun updateProfileImage() {
        val sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val selectedAvatarResId = sharedPref.getInt("selectedAvatarResId", R.drawable.avatar)
        val profile = findViewById<ImageView>(R.id.profile_image)
        profile.setImageResource(selectedAvatarResId)
    }

    private fun tabLayoutSetUp() {
        val tabLayout = findViewById<TabLayout>(R.id.tab)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = Tab_Adapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Recent"
                1 -> tab.text = "Upcoming"
                else -> tab.text = "News"
            }
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun drawerSetUp() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navLinearLayout: LinearLayout = findViewById(R.id.nav_linear_layout)

        if (navLinearLayout.childCount > 0) {
            selectNavItem(navLinearLayout.getChildAt(0) as ImageView, navLinearLayout)
        }

        for (i in 0 until navLinearLayout.childCount) {
            val child = navLinearLayout.getChildAt(i)
            child.setOnClickListener { view ->
                selectNavItem(view as ImageView, navLinearLayout)
            }
        }

        blurView = findViewById(R.id.blurView)
        val radius = 5f
        val decorView = window.decorView
        blurView.setupWith(decorView.findViewById(android.R.id.content) as ViewGroup)
            .setFrameClearDrawable(decorView.background)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                blurView.visibility = View.VISIBLE
                blurView.alpha = slideOffset
            }

            override fun onDrawerOpened(drawerView: View) {
                blurView.visibility = View.VISIBLE
                blurView.alpha = 1f
            }

            override fun onDrawerClosed(drawerView: View) {
                blurView.visibility = View.GONE
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        navigationView = findViewById(R.id.navigation_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun selectNavItem(selectedView: ImageView, navLinearLayout: LinearLayout) {
        val typedValue = TypedValue()
        val theme = selectedView.context.theme
        theme.resolveAttribute(R.attr.nav_item_background, typedValue, true)
        val navItemBackground = typedValue.resourceId

        for (i in 0 until navLinearLayout.childCount) {
            val child = navLinearLayout.getChildAt(i) as ImageView
            child.isSelected = false
            child.setBackgroundResource(navItemBackground)
        }
        selectedView.isSelected = true
        selectedView.setBackgroundResource(navItemBackground)
    }

    private fun themeToggle() {
        val theme = findViewById<ImageView>(R.id.theme)
        theme.setOnClickListener {
            toggleTheme()
            closeDrawer()
        }
    }

    private fun toggleTheme() {
        val sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putInt("theme", AppCompatDelegate.MODE_NIGHT_YES)
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        editor.apply()
        recreate()
    }


    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun showRateUsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.rate_us_dialog, null)
        val ratingBar: RatingBar = dialogView.findViewById(R.id.rating_bar)
        val submitButton: Button = dialogView.findViewById(R.id.submit_button)
        val dismissButton: Button = dialogView.findViewById(R.id.dismiss_button)

        val current: Float = ratingBar.rating

        val anim = ObjectAnimator.ofFloat(ratingBar, "rating", current, 5f)
        anim.duration = 1000
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                ratingBar.rating = 0f
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        anim.start()

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()



        submitButton.setOnClickListener {
            dialog.dismiss()
        }

        dismissButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
