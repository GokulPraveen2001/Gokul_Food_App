package com.internshala.gokusfoodapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.R
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {

    lateinit var otp: EditText
    lateinit var newpassword: EditText
    lateinit var confirmpassword: EditText
    lateinit var submit: Button
    lateinit var resettoolbar: androidx.appcompat.widget.Toolbar
    lateinit var  resetrequest: RequestQueue
    lateinit var resetmain: JSONObject
    lateinit var resetcredentials: JSONObject


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        resettoolbar = findViewById(R.id.resettoolbar)
        otp = findViewById(R.id.etotp)
        newpassword = findViewById(R.id.etnewpassword)
        confirmpassword = findViewById(R.id.etconfirmresetpassword)
        submit = findViewById(R.id.btnsubmit)

        settoolbar()

        submit.setOnClickListener {
            var enteredotp = otp.text.toString()
            var enteredpassword =newpassword.text.toString()
            var enteredconfirmedpassword=confirmpassword.text.toString()
            if (enteredotp.length == 4 && enteredpassword.length > 4 && enteredotp.isEmpty() == false && enteredpassword.isEmpty() == false)
            {if(enteredpassword==enteredconfirmedpassword) {
                var typeofservice = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activenetwork = typeofservice.activeNetwork
                var activenetworkcapability = typeofservice.getNetworkCapabilities(activenetwork)
                if (activenetworkcapability != null && activenetworkcapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                 {
                    val resetdetails = JSONObject()
                    resetdetails.put("mobile_number", intent.getStringExtra("usermobilenumber"))
                    resetdetails.put("password", enteredpassword)
                     resetdetails.put("otp",enteredotp)
                    resetrequest = Volley.newRequestQueue(this@ResetPassword as Context)
                    var url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    var request6 = object : JsonObjectRequest(Request.Method.POST, url, resetdetails,
                            Response.Listener {
                                try {

                                    resetmain = it.getJSONObject("data")
                                    val successvalue = resetmain.getBoolean("success")
                                    if (successvalue == true)
                                    {
                                          Toast.makeText(this@ResetPassword,"Password has succesfully changed",Toast.LENGTH_SHORT).show()
                                          startActivity(Intent(this@ResetPassword, Login::class.java))

                                    } else {
                                        Toast.makeText(this@ResetPassword, "Wrong OTP entered!", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e1: Exception) {
                                    Toast.makeText(this@ResetPassword, "Response Error", Toast.LENGTH_LONG)
                                        .show()
                                }

                            }
                            ,
                            Response.ErrorListener {
                                Toast.makeText(this@ResetPassword ,"Volley Error", Toast.LENGTH_LONG).show()
                            }) {
                            override fun getHeaders(): HashMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-Type"] = "application/json"
                                headers["token"] = "c604f1ab9fc2b3"
                                return headers
                            }


                        }
                resetrequest.add(request6)
                } else {
                    var connectiondialog = AlertDialog.Builder(this@ResetPassword as Context)
                    connectiondialog.setTitle("Connection Lost")
                    connectiondialog.setMessage("Internet Connection not Found")
                    connectiondialog.setPositiveButton("Open Settings")
                    { _, _ ->

                        var settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsintent)
                        finish()

                    }
                    connectiondialog.setNegativeButton("Exit App")
                    { _, _ ->

                        ActivityCompat.finishAffinity(this@ResetPassword as Activity)

                    }
                    connectiondialog.create()
                    connectiondialog.show()
                }
            }
                else{Toast.makeText(this@ResetPassword,"Confirmed Password not matching with the new password.",Toast.LENGTH_SHORT).show()}
            } else {
                Toast.makeText(this@ResetPassword, "Credential requirements not met.", Toast.LENGTH_LONG).show()
            }
        }

    }




    fun settoolbar()
    {setSupportActionBar(resettoolbar)
        supportActionBar?.setTitle("Reset Password")}

}