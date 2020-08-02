package com.internshala.gokusfoodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.activity.Home
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.internshala.gokusfoodapp.DataClass.Restaurant
import com.internshala.gokusfoodapp.Database.Favorites.FavoritesDatabase
import com.internshala.gokusfoodapp.adapter.HomeAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


class AllRestaurants: Fragment() {

    lateinit var homerecycler: RecyclerView

    var restaurantlist= arrayListOf<Restaurant>()
    lateinit var progressbar:ProgressBar
    lateinit var request:RequestQueue
    lateinit var main:JSONObject
    lateinit var restaurantarray:JSONArray
    lateinit var restaurants:JSONObject
var costsorting=kotlin.Comparator<Restaurant> { restaurant1, restaurant2 ->
    restaurant1.singlecost.compareTo(restaurant2.singlecost,ignoreCase = true)
}
    var rating=kotlin.Comparator<Restaurant> { restaurant1, restaurant2 ->
        restaurant1.rating.compareTo(restaurant2.rating,ignoreCase = true)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var restaurantsview = inflater.inflate(R.layout.allrestaurants, container, false)
       homerecycler=restaurantsview.findViewById(R.id.allrestaurantsrecycler)
        progressbar=restaurantsview.findViewById(R.id.allrestaurantsprogressbar)
        homerecycler.layoutManager=LinearLayoutManager(activity)

        progressbar.visibility=View.VISIBLE
        setHasOptionsMenu(true)


        var typeofservice = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activenetwork = typeofservice.activeNetwork
        var activenetworkcapability = typeofservice.getNetworkCapabilities(activenetwork)
         if (activenetworkcapability != null && activenetworkcapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
         {
            request = Volley.newRequestQueue(activity as Context)
            var url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            var request1 = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        progressbar.visibility = View.GONE
                        main = it.getJSONObject("data")
                        val successvalue = main.getBoolean("success")
                        if (successvalue == true) {
                            restaurantarray = main.getJSONArray("data")
                            for (i in 0 until restaurantarray.length()) {
                                restaurants = restaurantarray.getJSONObject(i)
                                val resobj = Restaurant(
                                    restaurants.getString("id"),
                                    restaurants.getString("name"),
                                    restaurants.getString("rating"),
                                    restaurants.getString("cost_for_one"),
                                    restaurants.getString("image_url")
                                )
                                restaurantlist.add(resobj)
                            }

                            homerecycler.adapter = HomeAdapter(activity as Context, restaurantlist)
                        } else {
                            Toast.makeText(activity, "Token Error", Toast.LENGTH_LONG).show()
                        }
                    } catch (e1: Exception) {
                        Toast.makeText(activity, "Response Error", Toast.LENGTH_LONG).show()
                    }

                }
                ,
                Response.ErrorListener {
                    if(activity!=null){Toast.makeText(activity, "Volley Error", Toast.LENGTH_LONG).show()}
                }) {
                override fun getHeaders(): HashMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "c604f1ab9fc2b3"
                    return headers
                }


            }
            request.add(request1)
        } else {
            var connectiondialog = AlertDialog.Builder(activity as Context)
            connectiondialog.setTitle("Connection Lost")
            connectiondialog.setMessage("Internet Connection not Found")
            connectiondialog.setPositiveButton("Open Settings")
            { _, _ ->

                var settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                activity!!.finish()

            }
            connectiondialog.setNegativeButton("Exit App")
            { _, _ ->

                ActivityCompat.finishAffinity(activity as Activity)

            }
            connectiondialog.create()
            connectiondialog.show()
        }




        return restaurantsview
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {

        inflater.inflate(R.menu.restaurantssortingmenuitem,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {

        when(item.itemId)
        {
            R.id.costsortascending->{
                                      Collections.sort(restaurantlist,costsorting)
                                    }


            R.id.costsortdescending->{
                                       Collections.sort(restaurantlist,costsorting)
                                       restaurantlist.reverse()
                                      }

            R.id.sortbyrating->{
                                  Collections.sort(restaurantlist,rating)
                                  restaurantlist.reverse()
                               }
        }
        homerecycler.adapter?.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


}