package com.codingtroops.foodies.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingtroops.foodies.model.FoodItem

@Dao
interface FoodDao {
    @Query("SELECT * FROM food")
    suspend fun getAll(): List<FoodItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(items: List<FoodItem>)
}