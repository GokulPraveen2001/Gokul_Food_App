package com.internshala.gokusfoodapp.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.gokusfoodapp.Database.Favorites.FavoritesDatabase
import com.internshala.gokusfoodapp.Database.Favorites.FavoritesEntity
import com.internshala.gokusfoodapp.R
import com.internshala.gokusfoodapp.adapter.FavoriteRestaurantsAdapter


class Favorites : Fragment()
{
    lateinit var favrecycler:RecyclerView
     var favlist= listOf<FavoritesEntity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var favoriteview:View
       favoriteview=inflater.inflate(R.layout.favorite, container, false)

        favrecycler=favoriteview.findViewById(R.id.favrecycler)

        favlist=favasync(activity as Context).execute().get()

        favrecycler.layoutManager=LinearLayoutManager(activity)
        favrecycler.adapter=FavoriteRestaurantsAdapter(activity as Context,favlist)

        return favoriteview
    }

    class favasync(context: Context) : AsyncTask<Void, Void, List<FavoritesEntity>>()
    {
        var favdb = Room.databaseBuilder(context, FavoritesDatabase::class.java, "favdb").build()
        override fun doInBackground(vararg p0: Void?): List<FavoritesEntity>
        {
            return favdb.favdao().displayfavrestaurants()
        }

    }
}



