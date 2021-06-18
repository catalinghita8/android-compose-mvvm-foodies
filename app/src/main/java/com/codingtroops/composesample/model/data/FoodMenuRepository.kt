package com.codingtroops.composesample.model.data

import com.codingtroops.composesample.model.response.FoodCategory

class FoodMenuRepository(private val foodMenuApi: FoodMenuApi = FoodMenuApi()) {

    suspend fun getFoodCategories(): List<FoodCategory> = foodMenuApi.getFoodMenu().categories

}