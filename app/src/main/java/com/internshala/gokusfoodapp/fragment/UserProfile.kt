package com.internshala.gokusfoodapp.fragment

import android.app.ActionBar
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.internshala.gokusfoodapp.R

class UserProfile : Fragment() {

lateinit var name:TextView
    lateinit var number:TextView
    lateinit var email:TextView
    lateinit var address:TextView
    lateinit var sharedpref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       var Userview=inflater.inflate(R.layout.user_profile, container, false)

        name=Userview.findViewById(R.id.txtusersname)
        number=Userview.findViewById(R.id.txtusersmobilenumber)
        email=Userview.findViewById(R.id.txtusersemail)
        address=Userview.findViewById(R.id.txtusersdeliveryaddress)

        sharedpref=activity!!.getSharedPreferences(getString(R.string.file1),Context.MODE_PRIVATE)

        name.text=sharedpref.getString("name","Gowrika Praveen")
        number.text=sharedpref.getString("email","gowrikapc26102001@gmail.com")
       email.text=sharedpref.getString("mobilenumber","9847962922")
        address.text=sharedpref.getString("address","Al Qusais,Dubai")


        return Userview
    }


    }
