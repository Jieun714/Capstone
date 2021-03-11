package com.example.capstone_navi_tab.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone_navi_tab.LoginActivity
import com.example.capstone_navi_tab.MainActivity
import com.example.capstone_navi_tab.R
import com.example.capstone_navi_tab.databinding.ActivityLoginBinding
import com.example.capstone_navi_tab.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {
    lateinit var binding:FragmentSettingsBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            activity?.let {
                startActivity(Intent(context,LoginActivity::class.java))
            }
        }*/
    }
}