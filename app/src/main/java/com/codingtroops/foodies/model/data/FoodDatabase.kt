package com.codingtroops.foodies.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingtroops.foodies.model.FoodItem
import javax.inject.Singleton

@Singleton
@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
abstract class FoodDatabase: RoomDatabase() {
    abstract val dao: FoodDao
}