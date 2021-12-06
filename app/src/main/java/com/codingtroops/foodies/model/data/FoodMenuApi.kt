package com.codingtroops.foodies.model.data

import com.codingtroops.foodies.di.IoDispatcher
import com.codingtroops.foodies.model.response.FoodCategoriesResponse
import com.codingtroops.foodies.model.response.MealsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodMenuApi @Inject constructor(private val service: Service,
                                      @IoDispatcher private val dispatcher: CoroutineDispatcher): IFoodMenuApi {
    override suspend fun getFoodCategories(): FoodCategoriesResponse {
        return withContext(dispatcher) {
            service.getFoodCategories()
        }
    }
    override suspend fun getMealsByCategory(categoryId: String): MealsResponse =
        service.getMealsByCategory(categoryId)

    interface Service {
        @GET("categories.php")
        suspend fun getFoodCategories(): FoodCategoriesResponse

        @GET("filter.php")
        suspend fun getMealsByCategory(@Query("c") categoryId: String): MealsResponse
    }

    companion object {
        const val API_URL = "https://www.themealdb.com/api/json/v1/1/"
    }
}

interface IFoodMenuApi {
    suspend fun getFoodCategories(): FoodCategoriesResponse
    suspend fun getMealsByCategory(categoryId: String): MealsResponse
}


