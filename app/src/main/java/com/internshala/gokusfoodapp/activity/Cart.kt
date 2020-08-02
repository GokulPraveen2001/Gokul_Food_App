package com.internshala.gokusfoodapp.activity

import android.annotation.SuppressLint
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
import android.widget.TextView
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
import com.internshala.gokusfoodapp.DataClass.Cartitem
import com.internshala.gokusfoodapp.DataClass.RestaurantFoodItem
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.adapter.CartAdapter
import com.internshala.gokusfoodapp.adapter.RestaurantFoodItemsAdapter
import org.json.JSONArray
import org.json.JSONObject

class Cart : AppCompatActivity()
{
lateinit var carttoolbar:androidx.appcompat.widget.Toolbar
    lateinit var cartrecycler:RecyclerView
   var cartitemlist= arrayListOf<Cartitem>()
   lateinit var cartitemidlist:ArrayList<String>
    lateinit var restaurantname:TextView
    lateinit var cartprogressbar:ProgressBar
    lateinit var totalamount:Button
    lateinit var itemrequest:RequestQueue
    lateinit var carthead: JSONObject
    lateinit var cartitemarray: JSONArray
    lateinit var cartitems: JSONObject
    lateinit var restaurantid:String
    lateinit var orderrequest:RequestQueue
    lateinit var share:SharedPreferences

var totalcost=0






    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart)


carttoolbar=findViewById(R.id.carttoolbar)
        cartrecycler=findViewById(R.id.cartrecycler)
        restaurantname=findViewById(R.id.txtorderrestaurant)
        cartprogressbar=findViewById(R.id.cartprogress)
        totalamount=findViewById(R.id.btnorderbutton)
        share=getSharedPreferences(getString(R.string.file1),Context.MODE_PRIVATE)
        cartrecycler.layoutManager=LinearLayoutManager(this@Cart)



        setupcarttoolbar()
        cartprogressbar.visibility= View.VISIBLE


        restaurantid= intent.getStringExtra("cartrestaurantid").toString()
        cartitemidlist= intent.getStringArrayListExtra("cartitemsidlist") as ArrayList<String>
        restaurantname.text= "Ordering from:"+intent.getStringExtra("cartrestaurantname").toString()


        var typesofservice =getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activenetworks = typesofservice.activeNetwork
        var activenetworkscapability = typesofservice.getNetworkCapabilities(activenetworks)
        if (activenetworkscapability != null && activenetworkscapability.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        ) {
            itemrequest = Volley.newRequestQueue(this@Cart as Context)
            var url = "http://13.235.250.119/v2/restaurants/fetch_result/${restaurantid}"
            var request2 = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                     cartprogressbar.visibility = View.GONE
                        carthead = it.getJSONObject("data")
                        val successvalue = carthead.getBoolean("success")
                        if (successvalue == true) {
                            cartitemarray = carthead.getJSONArray("data")
                            for (i in 0 until cartitemarray.length()) {
                                cartitems = cartitemarray.getJSONObject(i)
                                if(cartitemidlist.contains(cartitems.getString("id")))
                                {
                                    val cartitemobj = Cartitem(
                                        cartitems.getString("id"),
                                        cartitems.getString("name"),
                                        cartitems.getString("cost_for_one"),
                                        cartitems.getString("restaurant_id")
                                    )
                                    cartitemlist.add(cartitemobj)
                                    totalcost=totalcost+cartitems.getString("cost_for_one").toInt()
                                }
                                }

                            cartrecycler.adapter=CartAdapter(this@Cart as Context,cartitemlist)
                            totalamount.text="Place Order(Total:Rs."+totalcost.toString()+")"

                        } else {
                            Toast.makeText(
                                this@Cart,
                                "Token Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }catch (e1: Exception) {
                        Toast.makeText(
                            this@Cart,
                            "Response Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                ,
                Response.ErrorListener {
                    Toast.makeText(
                        this@Cart,
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
            var connectiondialogs = AlertDialog.Builder(this@Cart as Context)
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

                ActivityCompat.finishAffinity(this@Cart as Activity)

            }
            connectiondialogs.create()
            connectiondialogs.show()
        }



totalamount.setOnClickListener {

    var typeofservice =getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var activenetwork = typesofservice.activeNetwork
    var activenetworkcapability = typesofservice.getNetworkCapabilities(activenetwork)
    if (activenetworkcapability != null && activenetworkcapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    {
        orderrequest = Volley.newRequestQueue(this@Cart as Context)
        var urls = " http://13.235.250.119/v2/place_order/fetch_result/"
        var orderitems=JSONObject()
        orderitems.put("user_id",share.getString("user_id","12345"))
        orderitems.put("restaurant_id",restaurantid)
        orderitems.put("total_cost",totalcost)
        var itemslists=JSONArray()
        for(fooditem in cartitemidlist )
        {
            val singleitem=JSONObject()
            singleitem.put("food_item_id",fooditem)
            itemslists.put(singleitem)
        }
        orderitems.put("food",itemslists)

        var request3 = object : JsonObjectRequest(Request.Method.POST, urls, orderitems,
            Response.Listener {
                try {

                    carthead = it.getJSONObject("data")
                    val successvalue = carthead.getBoolean("success")
                    if (successvalue == true)
                    {
                       var intent=Intent(this@Cart,OrderConfirmation::class.java)
                        startActivity(intent)
                        finishAffinity()

                    } else {
                        Toast.makeText(
                            this@Cart,
                            "Order Confirmation Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }catch (e1: Exception) {
                    Toast.makeText(
                        this@Cart,
                        "Response Error",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            ,
            Response.ErrorListener {
                Toast.makeText(
                    this@Cart,
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
        orderrequest.add(request3)
    } else {
        var connectiondialogs = AlertDialog.Builder(this@Cart as Context)
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

            ActivityCompat.finishAffinity(this@Cart as Activity)

        }
        connectiondialogs.create()
        connectiondialogs.show()
    }

}








    }

    fun setupcarttoolbar()
    {setSupportActionBar(carttoolbar)
        supportActionBar?.setTitle("Cart")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){

            Toast.makeText(this@Cart,"Cart Emptied", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Cart, Home::class.java))
            finishAffinity()
        }
        return super.onOptionsItemSelected(item)

    }
    override fun onBackPressed() {

        Toast.makeText(this@Cart,"Cart Emptied", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@Cart, Home::class.java))
        finishAffinity()

    }


    }







