package com.example.atry

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity(){

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle // Declare toggle as a class member
    private lateinit var blurView: BlurView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setup the DrawerToggle
//        toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
        drawerSetUp()

        // Set up TabLayout and ViewPager
        tabLayoutSetUp()
        themeToggle()

        // Set toolbar title
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "CrickLive"


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
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        recreate()
    }

    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }


}
