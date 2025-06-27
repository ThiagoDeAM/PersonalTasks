package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Activity responsável pelo login do usuário usando Firebase Authentication
class LoginActivity : AppCompatActivity() {

    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val signInCoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        setSupportActionBar(alb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.login)

        alb.signUpBt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Botão de Login
        alb.signInBt.setOnClickListener {
            signInCoroutineScope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    alb.emailLoginEt.text.toString(),
                    alb.passwordLoginEt.text.toString()
                ).addOnFailureListener { // Caso de falha
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed. Cause: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener { // Caso de sucesso
                    Toast.makeText(
                        this@LoginActivity,
                        "Login successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    openMainActivity()
                }
            }
        }

        // Botão para redefinição de senha
        alb.resetPasswordBt.setOnClickListener {
            signInCoroutineScope.launch {
                val email = alb.emailLoginEt.text.toString()
                if (email.isNotEmpty()) {
                    Firebase.auth.sendPasswordResetEmail(email)
                }
            }
        }
    }

    // Verifica se o usuário já está logado ao iniciar a Activity
    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            openMainActivity()
        }
    }

    // Abre a tela principal e encerra a LoginActivity
    private fun openMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}