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
import android.widget.TextView
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

class NewRegistration : AppCompatActivity() {
    lateinit var Toolbar:androidx.appcompat.widget.Toolbar
    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var mobilenumber:EditText
    lateinit var location:EditText
    lateinit var password: EditText
    lateinit var confirmpassword:EditText
    lateinit var register: Button
    lateinit var  usercredentialrequest: RequestQueue
    lateinit var usermains: JSONObject
    lateinit var usercredentials: JSONObject
    lateinit var sharedpref: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_registration)
    Toolbar=findViewById(R.id.registertoolbar)
        name=findViewById(R.id.etname)
        email=findViewById(R.id.etemail)
        mobilenumber=findViewById(R.id.etmobilenumber)
        location=findViewById(R.id.etdeliveryaddress)
        password=findViewById(R.id.etregisterpassword)
        confirmpassword=findViewById(R.id.etconfirmpassword)
        register=findViewById(R.id.btnregister)
        sharedpref=getSharedPreferences(getString(R.string.file1),Context.MODE_PRIVATE)

        setSupportActionBar(Toolbar)
        supportActionBar?.setTitle("Registration")

       register.setOnClickListener {
           var enteredname=name.text.toString().trim()
           var enteredemail=email.text.toString().trim()
           var enteredmobilenumber=mobilenumber.text.toString()
           var enteredlocation=location.text.toString()
           var enteredpassword=password.text.toString().trim()
           var enteredconfirmpassword=confirmpassword.text.toString().trim()

           if (enteredname.length>2 && enteredpassword.length>4 && enteredmobilenumber.length == 10 )
           {
               if (enteredpassword == enteredconfirmpassword)
               {
                   var typeofservice = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                   var activenetwork = typeofservice.activeNetwork
                   var activenetworkcapability = typeofservice.getNetworkCapabilities(activenetwork)
                   if (activenetworkcapability != null && activenetworkcapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                   {
                       val useraccountdetails = JSONObject()
                       useraccountdetails.put("name", enteredname)
                       useraccountdetails.put("mobile_number", enteredmobilenumber)
                       useraccountdetails.put("password", enteredpassword)
                       useraccountdetails.put("address", enteredlocation)
                       useraccountdetails.put("email", enteredemail)
                       usercredentialrequest = Volley.newRequestQueue(this@NewRegistration as Context)
                       var url = " http://13.235.250.119/v2/register/fetch_result"
                       var request4 = object : JsonObjectRequest(Request.Method.POST, url,useraccountdetails,
                               Response.Listener {
                                   try {

                                       usermains = it.getJSONObject("data")
                                       val successvalue = usermains.getBoolean("success")
                                       if (successvalue == true) {
                                           usercredentials = usermains.getJSONObject("data")
                                           sharedpref.edit().putString("user_id", usercredentials.getString("user_id")).apply()
                                           sharedpref.edit().putString("name", usercredentials.getString("name")).apply()
                                           sharedpref.edit().putString("email", usercredentials.getString("email")).apply()
                                           sharedpref.edit().putString("mobilenumber", usercredentials.getString("mobile_number")).apply()
                                           sharedpref.edit().putString("address", usercredentials.getString("address")).apply()
                                          Toast.makeText(this@NewRegistration,"Registration Success",Toast.LENGTH_SHORT).show()
                                           startActivity(Intent(this@NewRegistration, Home::class.java))
                                           sharedpref.edit().putBoolean("Is Logged In", true).apply()
                                       } else {
                                           Toast.makeText(this@NewRegistration, "Registration Failed!", Toast.LENGTH_LONG).show()
                                       }
                                   } catch (e1: Exception)
                                   {
                                       Toast.makeText(this@NewRegistration, "Response Error", Toast.LENGTH_LONG).show()
                                   }

                               }
                               ,
                               Response.ErrorListener {
                                   Toast.makeText(this@NewRegistration, "Volley Error", Toast.LENGTH_LONG).show()})
                               {
                               override fun getHeaders(): HashMap<String, String> {
                                   val headers = HashMap<String, String>()
                                   headers["Content-Type"] = "application/json"
                                   headers["token"] = "c604f1ab9fc2b3"
                                   return headers
                               }


                           }
                       usercredentialrequest.add(request4)
                   } else {
                       var connectiondialog = AlertDialog.Builder(this@NewRegistration as Context)
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

                           ActivityCompat.finishAffinity(this@NewRegistration as Activity)

                       }
                       connectiondialog.create()
                       connectiondialog.show()
                   }

               }
           else {
               Toast.makeText(this@NewRegistration, "Password Confirmation went wrong!", Toast.LENGTH_LONG).show()
           }
       }
           else
            {
                Toast.makeText(this@NewRegistration,"Registration requirements not met", Toast.LENGTH_LONG).show()
            }
        }



    }

    override fun onBackPressed() {

        startActivity(Intent(this@NewRegistration,Login::class.java))

    }

}