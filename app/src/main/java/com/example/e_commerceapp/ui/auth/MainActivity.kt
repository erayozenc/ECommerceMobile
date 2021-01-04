package com.example.e_commerceapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.e_commerceapp.R
import com.example.e_commerceapp.ui.home.HomeActivity
import com.example.e_commerceapp.util.UserPreferences
import com.example.e_commerceapp.util.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences : UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences.authToken.asLiveData().observe(this, Observer {
            it?.let {
                startNewActivity(HomeActivity::class.java)
            }
        })
    }
}