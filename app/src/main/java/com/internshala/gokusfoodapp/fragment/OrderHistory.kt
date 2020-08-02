package com.internshala.gokusfoodapp.fragment

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.DataClass.OrderHistory
import com.internshala.gokusfoodapp.DataClass.RestaurantFoodItem
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.adapter.OrderHistoryAdapter
import com.internshala.gokusfoodapp.adapter.RestaurantFoodItemsAdapter
import org.json.JSONArray
import org.json.JSONObject


class OrderHistory : Fragment() {

    lateinit var orderhistoryrecycler:RecyclerView
    lateinit var orderhistoryprogressbar:ProgressBar
   lateinit var shared:SharedPreferences
    lateinit var userid:String
    var orderhistorylist= arrayListOf<OrderHistory>()
    lateinit var orderhistoryrequest: RequestQueue
    lateinit var historyhead: JSONObject
    lateinit var orderhistoryarray: JSONArray
    lateinit var orderhistoryitems: JSONObject


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var orderhistory:View
        orderhistory=inflater.inflate(R.layout.order_history, container, false)

        orderhistoryrecycler=orderhistory.findViewById(R.id.orderhistoryrecycler)
        orderhistoryrecycler.layoutManager=LinearLayoutManager(activity as Context)
        orderhistoryprogressbar=orderhistory.findViewById(R.id.orderhistoryprogress)
        shared=activity!!.getSharedPreferences(getString(R.string.file1),Context.MODE_PRIVATE)
        userid= shared.getString("user_id","12345").toString()

        orderhistoryprogressbar.visibility=View.VISIBLE



        var typesofservice =activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activenetworks = typesofservice.activeNetwork
        var activenetworkscapability = typesofservice.getNetworkCapabilities(activenetworks)
        if (activenetworkscapability != null && activenetworkscapability.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        ) {
            orderhistoryrequest = Volley.newRequestQueue(activity as Context)
            var url = " http://13.235.250.119/v2/orders/fetch_result/${userid}"
            var request2 = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        orderhistoryprogressbar.visibility = View.GONE
                        historyhead = it.getJSONObject("data")
                        val successvalue = historyhead.getBoolean("success")
                        if (successvalue == true) {
                            orderhistoryarray = historyhead.getJSONArray("data")
                            for (i in 0 until orderhistoryarray.length()) {
                                orderhistoryitems = orderhistoryarray.getJSONObject(i)
                                val itemobj = OrderHistory(
                                    orderhistoryitems.getString("order_id"),
                                    orderhistoryitems.getString("restaurant_name"),
                                    orderhistoryitems.getString("total_cost"),
                                    orderhistoryitems.getString("order_placed_at").substring(0,10)
                                )
                                orderhistorylist.add(itemobj)
                            }

                            orderhistoryrecycler.adapter =OrderHistoryAdapter(activity as Context, orderhistorylist,userid)
                            orderhistoryrecycler.addItemDecoration(DividerItemDecoration(orderhistoryrecycler.context,LinearLayoutManager(activity as Context).orientation))
                        } else {
                            Toast.makeText(
                                activity,
                                "Token Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }catch (e1: Exception) {
                        Toast.makeText(
                            activity,
                            "Response Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                ,
                Response.ErrorListener {
                    Toast.makeText(
                        activity,
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
            orderhistoryrequest.add(request2)
        } else {
            var connectiondialogs = AlertDialog.Builder(activity as Context)
            connectiondialogs.setTitle("Connection Lost")
            connectiondialogs.setMessage("Internet Connection not Found")
            connectiondialogs.setPositiveButton("Open Settings")
            { _, _ ->

                var settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
               activity!!.finish()

            }
            connectiondialogs.setNegativeButton("Exit App")
            { _, _ ->

                ActivityCompat.finishAffinity(activity as Activity)

            }
            connectiondialogs.create()
            connectiondialogs.show()
        }








        return orderhistory
    }


    }
