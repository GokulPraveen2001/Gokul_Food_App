package com.internshala.gokusfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.gokusfoodapp.DataClass.Cartitem
import com.internshala.gokusfoodapp.R

class CartAdapter (var context: Context, var itemlist:ArrayList<Cartitem>): RecyclerView.Adapter<CartAdapter.CartFoodItemsViewholder> ()
{
    class CartFoodItemsViewholder(var view: View): RecyclerView.ViewHolder(view)
    {
        var itemsname: TextView =view.findViewById(R.id.txtcartitemname)
        var itemsprice: TextView =view.findViewById(R.id.txtcartitemprice)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartFoodItemsViewholder
    {
        var cartitemview : View
      cartitemview= LayoutInflater.from(parent.context).inflate(R.layout.cartrecyclerviewlayout,parent,false)
        return CartFoodItemsViewholder(cartitemview)
    }

    override fun getItemCount(): Int
    {
        return itemlist.size
    }



    override fun onBindViewHolder(holder: CartFoodItemsViewholder, position: Int)
    {
        var cartlist=itemlist[position]
        holder.itemsname.text=cartlist.cartitemname
        holder.itemsprice.text="Rs. "+ cartlist.cartcostforoneitem
    }




}