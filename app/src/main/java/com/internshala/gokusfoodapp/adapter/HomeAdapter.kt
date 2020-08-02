package com.internshala.gokusfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.gokusfoodapp.DataClass.Restaurant
import com.internshala.gokusfoodapp.Database.Favorites.FavoritesDatabase
import com.internshala.gokusfoodapp.Database.Favorites.FavoritesEntity
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.activity.RestaurantFoodItems
import com.squareup.picasso.Picasso

class HomeAdapter(var context:Context,var itemlist:ArrayList<Restaurant>):RecyclerView.Adapter<HomeAdapter.viewholder>() {

    class viewholder(view:View):RecyclerView.ViewHolder(view)
    {var homerecycle:RelativeLayout=view.findViewById(R.id.relative)
        var resimage:ImageView=view.findViewById(R.id.imgrestaurantimage)
        var resname:TextView=view.findViewById(R.id.txtrestaurantname)
         var resprice:TextView=view.findViewById(R.id.txtrestaurantprice)
         var rating:TextView=view.findViewById(R.id.txtrating)
         var resfav:ImageView=view.findViewById(R.id.imghomefavorite)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
    var restaurantview:View
        restaurantview=LayoutInflater.from(parent.context).inflate(R.layout.allrestaurantsrecyclerviewlayout,parent,false)
    return  viewholder(restaurantview)}

    override fun getItemCount(): Int {
      return  itemlist.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int)
    {
        var reslist=itemlist[position]
        holder.resname.text=reslist.name
        holder.resprice.text="Rs."+reslist.singlecost+"/person"
        holder.rating.text=reslist.rating
        Picasso.get().load(reslist.imageurl).error(R.drawable.foodapplogo).into(holder.resimage)

        var presence = Async(context,FavoritesEntity(reslist.id,reslist.name,reslist.rating,reslist.singlecost,reslist.imageurl),1).execute().get()
        if(presence)
        {
            holder.resfav.setImageResource(R.drawable.favorites)
        }
        else
        {
            holder.resfav.setImageResource(R.drawable.blankfavorites)
        }

        holder.homerecycle.setOnClickListener {
            var intent= Intent(context, RestaurantFoodItems::class.java)
            intent.putExtra("restaid",reslist.id)
            intent.putExtra("resname",reslist.name)
            context.startActivity(intent)

        }

      holder.resfav.setOnClickListener {
       if(Async(context,FavoritesEntity(reslist.id,reslist.name,reslist.rating,reslist.singlecost,reslist.imageurl),1).execute().get())
       {
           val async=Async(context, FavoritesEntity(reslist.id, reslist.name, reslist.rating, reslist.singlecost, reslist.imageurl), 3).execute()
           val result=async.get()
           if(result)
           {
               Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
               holder.resfav.setImageResource(R.drawable.blankfavorites)

           }
           else
           {
               Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
           }




       }
       else
       { val async=Async(context,FavoritesEntity(reslist.id,reslist.name,reslist.rating,reslist.singlecost,reslist.imageurl),2).execute()
           val result=async.get()
           if(result)
           {
               Toast.makeText(context,"Added to Favorites", Toast.LENGTH_SHORT).show()
               holder.resfav.setImageResource(R.drawable.favorites)

           }
           else
           {
               Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
           }


       }
      }

    }


}
class Async(var contexts:Context,var restaurant:FavoritesEntity,var mode:Int): AsyncTask<Void, Void, Boolean>()
{var db= Room.databaseBuilder(contexts, FavoritesDatabase::class.java,"favdb").build()
    override fun doInBackground(vararg p0: Void?): Boolean
    {when(mode)
    {
        1-> {
            var presence=db.favdao().checkfavrespresence(restaurant.restaurantname)
            db.close()
            return presence!=null

       }

        2->{
            db.favdao().insert(restaurant)
        db.close()
            return true
        }

        3->{
            db.favdao().delete(restaurant)
            db.close()
            return true
        }
    }
return false

    }

}