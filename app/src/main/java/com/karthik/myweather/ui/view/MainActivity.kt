package com.karthik.myweather.ui.view

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.karthik.myweather.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        NavigationUI.setupActionBarWithNavController(
                this, Navigation.findNavController(navHostFragment.requireView()))
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(navHostFragment.requireView())
                .navigateUp() || super.onSupportNavigateUp()
    }
}