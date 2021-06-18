package com.codingtroops.composesample.model.response

import com.google.gson.annotations.SerializedName

data class FoodCategoriesResponse(val categories: List<FoodCategory>)

data class FoodCategory(
    @SerializedName("idCategory") val id: String,
    @SerializedName("strCategory") val name: String,
    @SerializedName("strCategoryThumb") val thumbnailUrl: String,
    @SerializedName("strCategoryDescription") val description: String
)
