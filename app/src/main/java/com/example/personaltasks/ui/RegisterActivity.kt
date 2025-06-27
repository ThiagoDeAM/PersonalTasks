package com.example.personaltasks.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityRegisterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Activity responsável pelo cadastro de um novo usuário no Firebase Authentication
class RegisterActivity : AppCompatActivity() {

    private val arb: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arb.root)

        setSupportActionBar(arb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.register)

        // Listener do botão de cadastro
        arb.signUpBt.setOnClickListener {
            val signUpCoroutineScope = CoroutineScope(Dispatchers.IO)

            signUpCoroutineScope.launch {
                // Chamada assíncrona para criar um novo usuário com e-mail e senha
                Firebase.auth.createUserWithEmailAndPassword(
                    arb.emailRegisterEt.text.toString(),
                    arb.passwordRegisterEt.text.toString()
                ).addOnFailureListener { // Em caso de falha no cadastro
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed. Cause: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener { // Em caso de sucesso no cadastro
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration successful.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}