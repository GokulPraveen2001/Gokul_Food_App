package com.internshala.gokusfoodapp.Database.Favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.internshala.gokusfoodapp.DataClass.Restaurant

@Dao
interface  FavoritesDAO
{
    @Insert
    fun insert(favrestaurant:FavoritesEntity)

    @Delete
    fun delete(delres:FavoritesEntity)

@Query("SELECT * FROM Favoritestable")
   fun displayfavrestaurants():List<FavoritesEntity>

    @Query("SELECT * FROM Favoritestable WHERE restaurantname=:resname")
    fun checkfavrespresence(resname:String):FavoritesEntity
}