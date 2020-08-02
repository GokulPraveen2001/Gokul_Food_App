package com.internshala.gokusfoodapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internshala.gokusfoodapp.R

class WelcomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)
    Handler().postDelayed({
        var intent=Intent(this@WelcomePage,Login::class.java)
        startActivity(intent)
        finish()},2000)

    }

}

