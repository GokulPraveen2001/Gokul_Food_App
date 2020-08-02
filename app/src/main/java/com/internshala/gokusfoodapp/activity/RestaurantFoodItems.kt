package com.internshala.gokusfoodapp.activity


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.DataClass.RestaurantFoodItem
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.adapter.RestaurantFoodItemsAdapter
import org.json.JSONArray
import org.json.JSONObject

class RestaurantFoodItems : AppCompatActivity()  {

    lateinit var fooditemsrecycler: RecyclerView
    var restaurantitemslist = arrayListOf<RestaurantFoodItem>()
    lateinit var fooditemstoolbar:androidx.appcompat.widget.Toolbar
    lateinit var proceedtocartlayout:RelativeLayout
    lateinit var itemprogressbar: ProgressBar
    lateinit var proceedtocartbutton: Button
    lateinit var restaurantid: String
    lateinit var restaurantname: String
    lateinit var itemrequest: RequestQueue
    lateinit var head: JSONObject
    lateinit var itemarray: JSONArray
    lateinit var items: JSONObject


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_food_items)

        fooditemstoolbar=findViewById(R.id.fooditemstoolbar)
        proceedtocartlayout=findViewById(R.id.proceedtocheckout)
        restaurantid = intent.getStringExtra("restaid").toString()
        restaurantname =intent.getStringExtra("resname").toString()
        fooditemsrecycler = findViewById(R.id.fooditemsrecycler)
        itemprogressbar = findViewById(R.id.itemprogress)
        proceedtocartbutton = findViewById(R.id.btncheckout)
        fooditemsrecycler.layoutManager = LinearLayoutManager(this@RestaurantFoodItems)


setuptoolbar()

        itemprogressbar.visibility = View.VISIBLE


        var typesofservice =getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activenetworks = typesofservice.activeNetwork
        var activenetworkscapability = typesofservice.getNetworkCapabilities(activenetworks)
        if (activenetworkscapability != null && activenetworkscapability.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        ) {
            itemrequest = Volley.newRequestQueue(this@RestaurantFoodItems as Context)
            var url = "http://13.235.250.119/v2/restaurants/fetch_result/${restaurantid}"
            var request2 = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        itemprogressbar.visibility = View.GONE
                        head = it.getJSONObject("data")
                        val successvalue = head.getBoolean("success")
                        if (successvalue == true) {
                            itemarray = head.getJSONArray("data")
                            for (i in 0 until itemarray.length()) {
                                var x = i + 1
                                items = itemarray.getJSONObject(i)
                                val itemobj = RestaurantFoodItem(
                                    "${x}",
                                    items.getString("id"),
                                    items.getString("name"),
                                    items.getString("cost_for_one"),
                                    items.getString("restaurant_id")
                                )
                                restaurantitemslist.add(itemobj)
                            }

                            fooditemsrecycler.adapter = RestaurantFoodItemsAdapter(this@RestaurantFoodItems as Context, restaurantitemslist, restaurantname,restaurantid,proceedtocartbutton)
                        } else {
                            Toast.makeText(
                                this@RestaurantFoodItems,
                                "Token Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }catch (e1: Exception) {
                        Toast.makeText(
                            this@RestaurantFoodItems,
                            "Response Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                ,
                Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantFoodItems,
                        "Volley Error",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                override fun getHeaders(): HashMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "c604f1ab9fc2b3"
                    return headers
                }


            }
            itemrequest.add(request2)
        } else {
            var connectiondialogs = AlertDialog.Builder(this@RestaurantFoodItems as Context)
            connectiondialogs.setTitle("Connection Lost")
            connectiondialogs.setMessage("Internet Connection not Found")
            connectiondialogs.setPositiveButton("Open Settings")
            { _, _ ->

                var settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                finish()

            }
            connectiondialogs.setNegativeButton("Exit App")
            { _, _ ->

                ActivityCompat.finishAffinity(this@RestaurantFoodItems as Activity)

            }
            connectiondialogs.create()
            connectiondialogs.show()
        }









    }

    fun setuptoolbar()
    {setSupportActionBar(fooditemstoolbar)
        supportActionBar?.setTitle(restaurantname)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){

    Toast.makeText(this@RestaurantFoodItems,"Cart Emptied",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RestaurantFoodItems, Home::class.java))
            finishAffinity()
        }
        return super.onOptionsItemSelected(item)

    }
    override fun onBackPressed() {

        Toast.makeText(this@RestaurantFoodItems,"Cart Emptied",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@RestaurantFoodItems, Home::class.java))
        finishAffinity()

    }

    }





