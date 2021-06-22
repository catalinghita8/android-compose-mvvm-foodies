package com.codingtroops.composesample.model.data

import com.codingtroops.composesample.model.response.FoodCategory

class FoodMenuRepository(private val foodMenuApi: FoodMenuApi = FoodMenuApi()) {

    private var cachedCategories: List<FoodCategory>? = null

    suspend fun getFoodCategories(): List<FoodCategory> {
        var cachedCategories = cachedCategories
        if (cachedCategories == null) {
            cachedCategories = foodMenuApi.getFoodMenu().categories
            this.cachedCategories = cachedCategories
        }
        return cachedCategories
    }

    companion object {
        val instance = FoodMenuRepository()
    }

}