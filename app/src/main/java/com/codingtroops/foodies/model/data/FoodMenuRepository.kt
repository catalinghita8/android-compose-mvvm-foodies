package com.codingtroops.foodies.model.data

import com.codingtroops.foodies.di.IoDispatcher
import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.response.FoodCategoriesResponse
import com.codingtroops.foodies.model.response.MealsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FoodMenuRepository @Inject constructor(private val foodMenuApi: IFoodMenuApi,
                                                  @IoDispatcher private val dispatcher: CoroutineDispatcher): FoodMenuRepositoryContract {

    private var cachedCategories: List<FoodItem>? = null

    override suspend fun getFoodCategories(): List<FoodItem> {
        return withContext(dispatcher) {
            var cachedCategories = cachedCategories
            if (cachedCategories == null) {
                cachedCategories = foodMenuApi.getFoodCategories().mapCategoriesToItems()
                this@FoodMenuRepository.cachedCategories = cachedCategories
            }
            return@withContext cachedCategories
        }
    }

    suspend fun getMealsByCategory(categoryId: String): List<FoodItem> {
        return withContext(dispatcher) {
            val categoryName = getFoodCategories().first { it.id == categoryId }.name
            return@withContext foodMenuApi.getMealsByCategory(categoryName).mapMealsToItems()
        }
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