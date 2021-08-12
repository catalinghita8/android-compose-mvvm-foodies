package com.codingtroops.foodies.model.data

import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.response.FoodCategoriesResponse
import com.codingtroops.foodies.model.response.MealsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FoodMenuRepository @Inject constructor(private val foodMenuApi: FoodMenuApi): FoodMenuRepositoryContract {

    private var cachedCategories: List<FoodItem>? = null

    override suspend fun getFoodCategories(): List<FoodItem> {
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

}

interface FoodMenuRepositoryContract {
    suspend fun getFoodCategories(): List<FoodItem>
}