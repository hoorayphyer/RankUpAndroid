package com.example.rankupandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rankupandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO 1. action bar text 2. options menu?
    }
}