package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        setSupportActionBar(alb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.login)

        alb.signUpBt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}