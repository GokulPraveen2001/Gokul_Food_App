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
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.DataClass.Restaurant
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.activity.Home
import com.internshala.gokusfoodapp.adapter.HomeAdapter
import org.json.JSONArray
import org.json.JSONObject

class Login : AppCompatActivity() {
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var fg: TextView
    lateinit var register: TextView
    lateinit var  credentialrequest:RequestQueue
    lateinit var usermain: JSONObject
    lateinit var usercredentials: JSONObject
    lateinit var sharedpref:SharedPreferences


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        var loggedinstatus:Boolean

        username = findViewById(R.id.etusername)
        password = findViewById(R.id.etloginpassword)
        login = findViewById(R.id.btnlogin)
        fg = findViewById(R.id.txtfgpassword)
        register = findViewById(R.id.txtregister)

        toolbar=findViewById(R.id.toolbar)
        sharedpref=getSharedPreferences(getString(R.string.file1),Context.MODE_PRIVATE)
        loggedinstatus=sharedpref.getBoolean("Is Logged In",false)



        if(loggedinstatus)
        {var loginintent=Intent(this@Login,Home::class.java)
        startActivity(loginintent)
        finish()
        }


        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Log In")



        login.setOnClickListener {
            var enteredusername=username.text.toString()
            var enteredpassword=password.text.toString()
            if (enteredusername.length == 10 && enteredpassword.length > 4&&enteredusername.isEmpty()==false&&enteredpassword.isEmpty()==false)
            {
                var typeofservice = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activenetwork = typeofservice.activeNetwork
                var activenetworkcapability = typeofservice.getNetworkCapabilities(activenetwork)
                if (activenetworkcapability != null && activenetworkcapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                {
                    val accountdetails = JSONObject()
                    accountdetails.put("mobile_number", enteredusername)
                    accountdetails.put("password", enteredpassword)
                    credentialrequest = Volley.newRequestQueue(this@Login as Context)
                    var url = "http://13.235.250.119/v2/login/fetch_result"
                    var request3 = object : JsonObjectRequest(Request.Method.POST, url, accountdetails,
                        Response.Listener {
                            try
                            {

                                usermain = it.getJSONObject("data")
                                val successvalue = usermain.getBoolean("success")
                                if (successvalue == true)
                                {
                                    usercredentials=usermain.getJSONObject("data")
                                    sharedpref.edit().putString("user_id",usercredentials.getString("user_id")).apply()
                                    sharedpref.edit().putString("name",usercredentials.getString("name")).apply()
                                    sharedpref.edit().putString("email",usercredentials.getString("email")).apply()
                                    sharedpref.edit().putString("mobilenumber",usercredentials.getString("mobile_number")).apply()
                                    sharedpref.edit().putString("address",usercredentials.getString("address")).apply()
                                    startActivity(Intent(this@Login,Home::class.java))
                                    sharedpref.edit().putBoolean("Is Logged In",true).apply()
                                }

                                 else
                                { Toast.makeText(this@Login, "Login Failed", Toast.LENGTH_LONG).show() }
                            } catch (e1: Exception)
                            {
                                Toast.makeText(this@Login, "Response Error", Toast.LENGTH_LONG).show()
                            }

                        }
                        ,
                        Response.ErrorListener {
                            Toast.makeText(this@Login, "Volley Error", Toast.LENGTH_LONG).show()
                        }) {
                        override fun getHeaders(): HashMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = "c604f1ab9fc2b3"
                            return headers
                        }


                    }
                    credentialrequest.add(request3)
                } else {
                    var connectiondialog = AlertDialog.Builder(this@Login as Context)
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

                        ActivityCompat.finishAffinity(this@Login as Activity)

                    }
                    connectiondialog.create()
                    connectiondialog.show()
                }

            }
         else
            {Toast.makeText(this@Login,"Login failed",Toast.LENGTH_LONG).show()}
        }



        register.setOnClickListener {
            var registerintent= Intent(this@Login,NewRegistration::class.java)
            startActivity(registerintent)

        }

        fg.setOnClickListener {
            var forgotintent=Intent(this@Login,ForgotPassword::class.java)
            startActivity(forgotintent)
        }




    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onPause()
    {
        super.onPause()
        finish()
    }

}