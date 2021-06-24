package com.codingtroops.composesample.model.response

import com.google.gson.annotations.SerializedName

data class FoodCategoriesResponse(val categories: List<FoodCategoryResponse>)
data class MealsResponse(val meals: List<MealResponse>)


data class FoodCategoryResponse(
    @SerializedName("idCategory") val id: String,
    @SerializedName("strCategory") val name: String,
    @SerializedName("strCategoryThumb") val thumbnailUrl: String,
    @SerializedName("strCategoryDescription") val description: String = ""
)

data class MealResponse(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumbnailUrl: String,
)
