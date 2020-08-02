package com.internshala.gokusfoodapp.Database.Favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favoritestable")
data class FavoritesEntity (
    @ColumnInfo var restaurantid:String,
    @PrimaryKey var restaurantname:String,
    @ColumnInfo var restaurantrating:String,
    @ColumnInfo var  costperperson:String,
    @ColumnInfo var restaurantimage:String
)