package com.example.smartparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginLog.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)

            startActivity(intent)
            finish()
        }


        registerLog.setOnClickListener {
            val intent = Intent(context, RegistrasiActivity::class.java)

            startActivity(intent)
        }

    }
}
