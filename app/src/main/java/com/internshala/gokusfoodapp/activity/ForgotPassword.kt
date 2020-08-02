package com.internshala.gokusfoodapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.app.AlertDialog
import android.content.SharedPreferences
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.R
import org.json.JSONObject
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue

class ForgotPassword : AppCompatActivity() {

   lateinit var logo:ImageView
lateinit var mobilenumber:EditText
    lateinit var email:EditText
    lateinit var Next:Button
    lateinit var  fgcredentialrequest: RequestQueue
    lateinit var fgusermain: JSONObject
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        logo=findViewById(R.id.imglogin)
        mobilenumber=findViewById(R.id.etusername)
        email=findViewById(R.id.etemail)
        Next=findViewById(R.id.btnnext)



       Next.setOnClickListener {
            var enteredmobilenumber=mobilenumber.text.toString()
            var enteredemail=email.text.toString()
            if (enteredmobilenumber.length == 10 && enteredmobilenumber.isEmpty()==false&&enteredemail.isEmpty()==false)
            {
                var typeofservice = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activenetwork = typeofservice.activeNetwork
                var activenetworkcapability = typeofservice.getNetworkCapabilities(activenetwork)
                if (activenetworkcapability != null && activenetworkcapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                {
                    val fgpassworddetails = JSONObject()
                    fgpassworddetails.put("mobile_number", enteredmobilenumber)
                   fgpassworddetails.put("email", enteredemail)
                    fgcredentialrequest = Volley.newRequestQueue(this@ForgotPassword as Context)
                    var url = "  http://13.235.250.119/v2/forgot_password/fetch_result"
                    var request5 = object : JsonObjectRequest(Request.Method.POST, url,fgpassworddetails,
                        Response.Listener {
                            try
                            {

                                fgusermain = it.getJSONObject("data")
                                val successvalue = fgusermain.getBoolean("success")
                                if (successvalue == true)
                                {   Toast.makeText(this@ForgotPassword,"OTP Sent Successfully to your registered EmailID!",Toast.LENGTH_LONG).show()
                                    var intent=Intent(this@ForgotPassword,ResetPassword::class.java)
                                    intent.putExtra("usermobilenumber",enteredmobilenumber)
                                    startActivity(intent)
                                    finish()

                                }

                                else
                                { Toast.makeText(this@ForgotPassword, "You do not have an account registered with this mobile number and email", Toast.LENGTH_LONG).show() }
                            } catch (e1: Exception)
                            {
                                Toast.makeText(this@ForgotPassword, "Response Error", Toast.LENGTH_LONG).show()
                            }

                        }
                        ,
                        Response.ErrorListener {
                            Toast.makeText(this@ForgotPassword, "Volley Error", Toast.LENGTH_LONG).show()
                        }) {
                        override fun getHeaders(): HashMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = "c604f1ab9fc2b3"
                            return headers
                        }


                    }
                    fgcredentialrequest.add(request5)
                } else {
                    var connectiondialog = AlertDialog.Builder(this@ForgotPassword as Context)
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

                        ActivityCompat.finishAffinity(this@ForgotPassword as Activity)

                    }
                    connectiondialog.create()
                    connectiondialog.show()
                }

            }
            else
            {Toast.makeText(this@ForgotPassword,"Invalid Credentials(Requirements not met)",Toast.LENGTH_LONG).show()}
        }

    }

    override fun onBackPressed() {

        startActivity(Intent(this@ForgotPassword,Login::class.java))

    }

}