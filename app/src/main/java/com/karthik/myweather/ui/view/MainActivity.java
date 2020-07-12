package com.karthik.myweather.ui.view;

import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.karthik.myweather.R;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        NavigationUI.setupActionBarWithNavController(
                this, Navigation.findNavController(findViewById(R.id.fragment_navHost)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(findViewById(R.id.fragment_navHost))
                .navigateUp() || super.onSupportNavigateUp();
    }
}