package com.internshala.gokusfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.gokusfoodapp.Database.Favorites.FavoritesEntity
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.activity.RestaurantFoodItems
import com.squareup.picasso.Picasso

class FavoriteRestaurantsAdapter(var context: Context,var favlist:List<FavoritesEntity>):RecyclerView.Adapter<FavoriteRestaurantsAdapter.favoritesrescontentholder>()
{
    class favoritesrescontentholder(var view: View):RecyclerView.ViewHolder(view)
    {
        var favrecycle: RelativeLayout =view.findViewById(R.id.relatives)
        var resimages: ImageView =view.findViewById(R.id.imgrestaurantimages)
        var resnames: TextView =view.findViewById(R.id.txtrestaurantnames)
        var resprices: TextView =view.findViewById(R.id.txtrestaurantprices)
        var ratings: TextView =view.findViewById(R.id.txtratings)
        var resfavs: ImageView =view.findViewById(R.id.imghomefavorites)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favoritesrescontentholder
    {
        var favview:View
        favview=LayoutInflater.from(parent.context).inflate(R.layout.favoritesrecyclerviewlayout,parent,false)
        return favoritesrescontentholder(favview)
    }

    override fun getItemCount(): Int
    {
     return  favlist.size
    }
;
    override fun onBindViewHolder(holder: favoritesrescontentholder, position: Int)
    {var favreslist=favlist[position]
        holder.resnames.text=favreslist.restaurantname
        holder.resprices.text="Rs."+favreslist.costperperson+"/person"
        holder.ratings.text=favreslist.restaurantrating
        Picasso.get().load(favreslist.restaurantimage).error(R.drawable.foodapplogo).into(holder.resimages)

        holder.favrecycle.setOnClickListener {
            var intent= Intent(context, RestaurantFoodItems::class.java)
            intent.putExtra("restaid",favreslist.restaurantid)
            intent.putExtra("resname",favreslist.restaurantname)
            context.startActivity(intent)
        }
    }
}