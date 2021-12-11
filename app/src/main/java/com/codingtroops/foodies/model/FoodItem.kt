package com.codingtroops.foodies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodItem(
    @PrimaryKey()
    @ColumnInfo(name = "r_id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnailUrl: String,
    @ColumnInfo(name = "description")
    val description: String = ""
)
