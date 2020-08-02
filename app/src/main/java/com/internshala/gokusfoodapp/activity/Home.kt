package com.internshala.gokusfoodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.fragment.*

class Home : AppCompatActivity() {
   lateinit var drawer:DrawerLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
lateinit var navigation:NavigationView
  lateinit  var sharedpref:SharedPreferences
    lateinit var accountname:TextView
    lateinit var accountnumber:TextView
    lateinit var navigationheader:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        HomeFragment()


        drawer=findViewById(R.id.homedrawer)
        toolbar=findViewById(R.id.toolbar)
        navigation=findViewById(R.id.navigationdrawer)
        sharedpref=getSharedPreferences(getString(R.string.file1), Context.MODE_PRIVATE)
        navigationheader=navigation.getHeaderView(0)
        accountname=navigationheader.findViewById(R.id.txtprofileid)
        accountnumber=navigationheader.findViewById(R.id.txtprofilenumber)
        accountname.text=sharedpref.getString("name","Gowrika Praveen")
        accountnumber.text=sharedpref.getString("mobilenumber","9847962922")



        setuptoolbar()
        var actionbardrawertoggle=ActionBarDrawerToggle(this@Home,drawer,R.string.OpenDrawer,R.string.CloseDrawer)
        drawer.addDrawerListener(actionbardrawertoggle)
        actionbardrawertoggle.syncState()


        navigation.setNavigationItemSelectedListener{
           it.isCheckable=true
            it.isChecked=true
            when(it.itemId){

               R.id.Home->{
                           supportFragmentManager.beginTransaction().replace(R.id.frame,AllRestaurants()).commit()
                           supportActionBar?.setTitle("All Restaurants")
                           toolbar.setBackgroundColor(resources.getColor((R.color.colorPrimary)))
                           drawer.closeDrawers()
                          }

               R.id.Profile->
               {
                   supportFragmentManager.beginTransaction().replace(R.id.frame,UserProfile()).commit()
                   supportActionBar?.setTitle("User Profile")
                   toolbar.setBackgroundColor(resources.getColor((R.color.Black)))
                   drawer.closeDrawers()
               }

              R.id.favorites->
              {
                  supportFragmentManager.beginTransaction().replace(R.id.frame,Favorites()).commit()
                  supportActionBar?.setTitle("Favorite Restaurants")
                  toolbar.setBackgroundColor(resources.getColor((R.color.colorPrimary)))
                  drawer.closeDrawers()

              }

              R.id.orderhistory->
              {
                  supportFragmentManager.beginTransaction().replace(R.id.frame,OrderHistory()).commit()
                  supportActionBar?.setTitle("Order History")
                  toolbar.setBackgroundColor(resources.getColor((R.color.colorPrimary)))
                  drawer.closeDrawers()

              }

              R.id.faq->
              {
                  supportFragmentManager.beginTransaction().replace(R.id.frame,FAQ()).commit()
                  supportActionBar?.setTitle("Frequently Asked Questions")
                  toolbar.setBackgroundColor(resources.getColor((R.color.colorPrimary)))
                  drawer.closeDrawers()
              }

              R.id.logout->
              {


                  var dialogbox=AlertDialog.Builder(this@Home)
                  dialogbox.setTitle("Confirmation")

                  dialogbox.setMessage("Are you sure you want to Log Out?")

                  dialogbox.setPositiveButton("Yes")
                  {_,_->
                      logout()

                      var logoutintent=Intent(this@Home,Login::class.java)
                      startActivity(logoutintent)
                      finish()
                  }

                  dialogbox.setNegativeButton("No")
                  {_,_->
                    drawer.closeDrawers()


                  }
                  dialogbox.create()
                  dialogbox.show()
              }

           }
       return@setNavigationItemSelectedListener true
        }




    }
       fun setuptoolbar()
     {
    setSupportActionBar(toolbar)
    supportActionBar?.setTitle("All Restaurants")
    supportActionBar?.setHomeButtonEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
     }



     fun HomeFragment()
     {supportFragmentManager.beginTransaction().replace(R.id.frame,AllRestaurants()).commit()

     }


    override fun onBackPressed()
    {
        var fragment=supportFragmentManager.findFragmentById(R.id.frame)
        if(fragment!=AllRestaurants())
        {
            supportFragmentManager.beginTransaction().replace(R.id.frame,AllRestaurants()).commit()
            supportActionBar?.setTitle("All Restaurants")
            toolbar.setBackgroundColor(resources.getColor((R.color.colorPrimary)))
        }
        else
        {
            super.onBackPressed()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId==android.R.id.home)
        {
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }


    fun logout()
    {
        sharedpref.edit().putBoolean("Is Logged In",false).apply()
    }

}