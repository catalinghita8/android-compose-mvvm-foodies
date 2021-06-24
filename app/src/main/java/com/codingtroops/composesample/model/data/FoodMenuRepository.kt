package com.codingtroops.composesample.model.data

import com.codingtroops.composesample.model.FoodItem
import com.codingtroops.composesample.model.response.FoodCategoriesResponse
import com.codingtroops.composesample.model.response.MealsResponse

class FoodMenuRepository(private val foodMenuApi: FoodMenuApi = FoodMenuApi()) {

    private var cachedCategories: List<FoodItem>? = null

    suspend fun getFoodCategories(): List<FoodItem> {
        var cachedCategories = cachedCategories
        if (cachedCategories == null) {
            cachedCategories = foodMenuApi.getFoodCategories().mapCategoriesToItems()
            this.cachedCategories = cachedCategories
        }
        return cachedCategories
    }

    suspend fun getMealsByCategory(categoryId: String): List<FoodItem> {
        val categoryName = getFoodCategories().first { it.id == categoryId }.name
        return foodMenuApi.getMealsByCategory(categoryName).mapMealsToItems()
    }


    private fun FoodCategoriesResponse.mapCategoriesToItems(): List<FoodItem> {
        return this.categories.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                description = category.description,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }

    private fun MealsResponse.mapMealsToItems(): List<FoodItem> {
        return this.meals.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }


    companion object {
        val instance = FoodMenuRepository()
    }

}