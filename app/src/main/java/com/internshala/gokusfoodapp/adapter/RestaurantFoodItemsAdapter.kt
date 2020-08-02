package com.internshala.gokusfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.internshala.gokusfoodapp.DataClass.RestaurantFoodItem
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.activity.Cart

class RestaurantFoodItemsAdapter(var context: Context,var fooditemlist:ArrayList<RestaurantFoodItem>,var restaurantsname:String,var restaurantsid:String,var proceedbutton:Button):RecyclerView.Adapter<RestaurantFoodItemsAdapter.RestaurantFoodItemsViewholder> ()
{
    var cartitemcount=0
    var cartitemsid= arrayListOf<String>()




    class RestaurantFoodItemsViewholder(view:View):RecyclerView.ViewHolder(view)
    {var itemnumber:TextView=view.findViewById(R.id.txtitemnumber)
        var itemname:TextView=view.findViewById(R.id.txtitemname)
        var itemprice:TextView=view.findViewById(R.id.txtitemprice)
        var  addtocartbutton:Button=view.findViewById(R.id.btncartbutton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantFoodItemsViewholder
    {
 var fooditemview :View
    fooditemview=LayoutInflater.from(parent.context).inflate(R.layout.restaurantfooditemsrecyclerviewlayout,parent,false)
        return RestaurantFoodItemsViewholder(fooditemview)
    }

    override fun getItemCount(): Int
    {
       return fooditemlist.size
    }



    override fun onBindViewHolder(holder: RestaurantFoodItemsViewholder, position: Int)
    {var items=fooditemlist[position]
       holder.itemnumber.text=items.itemnumber
        holder.itemname.text=items.itemname
        holder.itemprice.text="Rs."+items.costforoneitem
        holder.addtocartbutton.setTag(items.itemid)


        proceedbutton.visibility=View.INVISIBLE



        holder.addtocartbutton.text="Add"
        val addefaultcolor=ContextCompat.getColor(context, R.color.colorAccent)
        holder.addtocartbutton.setBackgroundColor(addefaultcolor)

    holder.addtocartbutton.setOnClickListener {

        if(holder.addtocartbutton.text=="Add")
        {   cartitemcount=cartitemcount+1

            cartitemsid.add(holder.addtocartbutton.getTag().toString())

            holder.addtocartbutton.text="Remove"
            val removecolor=ContextCompat.getColor(context, R.color.colorPrimary)
            holder.addtocartbutton.setBackgroundColor(removecolor)
        }
        else
        {
            cartitemcount=cartitemcount-1
            cartitemsid.remove(holder.addtocartbutton.getTag().toString())

            holder.addtocartbutton.text="Add"
            val addcolor=ContextCompat.getColor(context, R.color.colorAccent)
            holder.addtocartbutton.setBackgroundColor(addcolor)
        }

        if(cartitemcount>0)
        {
            proceedbutton.visibility=View.VISIBLE
        }

        else
        {
            proceedbutton.visibility=View.INVISIBLE
        }

    }



        proceedbutton.setOnClickListener {

            var intent=Intent(context,Cart::class.java)
            intent.putExtra("cartrestaurantname",restaurantsname)
            intent.putExtra("cartrestaurantid",restaurantsid)
            intent.putExtra("cartitemsidlist",cartitemsid)
            context.startActivity(intent)
        }

    }



}

