package com.internshala.gokusfoodapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.DataClass.OrderHistory
import com.internshala.gokusfoodapp.DataClass.ordereditems
import com.internshala.gokusfoodapp.R
import kotlinx.coroutines.channels.consumesAll
import org.json.JSONArray
import org.json.JSONObject

class OrderHistoryAdapter(var context: Context,var orderhistorylist:ArrayList<OrderHistory>,var Userid:String): RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewholder> ()
{
    var listofitems= arrayListOf<ordereditems>()
    lateinit var orderhistoryrequest: RequestQueue
    lateinit var historyhead: JSONObject
    lateinit var orderhistoryarray: JSONArray
    lateinit var orderhistoryitems: JSONObject

    class OrderHistoryViewholder(var view: View): RecyclerView.ViewHolder(view)
    {
 var orderedrestaurantname:TextView=view.findViewById(R.id.txtorderedrestaurantname)
        var ordereddate:TextView=view.findViewById(R.id.txtordereddate)
        var fooditems:RecyclerView=view.findViewById(R.id.fooditems)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OrderHistoryViewholder
    {
        var orderhistoryview : View
       orderhistoryview= LayoutInflater.from(parent.context).inflate(R.layout.orderhistorysinglerowrecyclerviewlayout,parent,false)
        return  OrderHistoryViewholder(orderhistoryview)
    }

    override fun getItemCount(): Int
    {
        return orderhistorylist.size
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder:  OrderHistoryViewholder, position: Int)
    {
var list=orderhistorylist[position]
        holder.orderedrestaurantname.text=list.restaurantsname
        var datetobeformatted=list.orderdate
        var formatteddate=datetobeformatted.replace("-","/")
        formatteddate=formatteddate.substring(0,6)+"20"+formatteddate.substring(6,8)
        holder.ordereddate.text=formatteddate

        holder.fooditems.layoutManager=LinearLayoutManager(context)


        var typesofservice =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activenetworks = typesofservice.activeNetwork
        var activenetworkscapability = typesofservice.getNetworkCapabilities(activenetworks)
        if (activenetworkscapability != null && activenetworkscapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
        {
            orderhistoryrequest = Volley.newRequestQueue(context as Context)
            var url = " http://13.235.250.119/v2/orders/fetch_result/${Userid}"
            var request2 = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {

                        historyhead = it.getJSONObject("data")
                        val successvalue = historyhead.getBoolean("success")
                        if (successvalue == true) {
                            orderhistoryarray = historyhead.getJSONArray("data")
                            orderhistoryitems = orderhistoryarray.getJSONObject(position)

                                var fooditems=orderhistoryitems.getJSONArray("food_items")
                                for(j in 0 until fooditems.length())
                                {
                                    var individualfooditem = fooditems.getJSONObject(j)
                                    val itemobj = ordereditems(
                                        individualfooditem.getString("food_item_id"),
                                        individualfooditem.getString("name"),
                                        individualfooditem.getString("cost")
                                    )
                                    listofitems.add(itemobj)
                                }


                           holder.fooditems.adapter =Orderedfooditemsadapter(context as Context, listofitems)
                        } else {
                            Toast.makeText(
                               context,
                                "Token Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }catch (e1: Exception) {
                        Toast.makeText(
                            context,
                            "Response Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                ,
                Response.ErrorListener {
                    Toast.makeText(
                        context,
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
            var connectiondialogs = AlertDialog.Builder(context as Context)
            connectiondialogs.setTitle("Connection Lost")
            connectiondialogs.setMessage("Internet Connection not Found")
            connectiondialogs.setPositiveButton("Open Settings")
            { _, _ ->

                var settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(settingsintent)


            }
            connectiondialogs.setNegativeButton("Exit App")
            { _, _ ->

                ActivityCompat.finishAffinity(context as Activity)

            }
            connectiondialogs.create()
            connectiondialogs.show()
        }











    }




}