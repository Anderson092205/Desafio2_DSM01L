package com.example.desafio2dsm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * MainActivity acts as a router: if the user is already logged in,
 * redirect to WelcomeActivity; otherwise send them to LoginActivity.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val destination = if (auth.currentUser != null) {
            WelcomeActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, destination).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}