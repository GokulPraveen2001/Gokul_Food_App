package com.internshala.gokusfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.gokusfoodapp.DataClass.ordereditems
import com.internshala.gokusfoodapp.R


class Orderedfooditemsadapter(var context: Context,var foodlist:ArrayList<ordereditems>) : RecyclerView.Adapter<Orderedfooditemsadapter.Orderedfooditemscontentholder>()
{
    class Orderedfooditemscontentholder(var view: View): RecyclerView.ViewHolder(view)
    {
        var itemsname: TextView =view.findViewById(R.id.txtcartitemname)
        var itemsprice: TextView =view.findViewById(R.id.txtcartitemprice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Orderedfooditemscontentholder
    {
        var orderedfooditemsview: View
        orderedfooditemsview= LayoutInflater.from(parent.context).inflate(R.layout.cartrecyclerviewlayout,parent,false)
        return  Orderedfooditemscontentholder( orderedfooditemsview)
    }

    override fun getItemCount(): Int
    {
        return foodlist.size
    }
    ;
    override fun onBindViewHolder(holder:Orderedfooditemscontentholder , position: Int)
    {
        var cartlist=foodlist[position]
        holder.itemsname.text=cartlist.itemname
        holder.itemsprice.text="Rs. "+ cartlist.itemcost
    }
}