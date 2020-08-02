package com.internshala.gokusfoodapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.internshala.gokusfoodapp.R

class OrderConfirmation : AppCompatActivity() {
    lateinit var okbutton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        okbutton=findViewById(R.id.txtok)

        okbutton.setOnClickListener {
            var intent= Intent(this@OrderConfirmation,Home::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}