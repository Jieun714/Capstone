package com.example.capstone_navi_tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_navi_tab.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}