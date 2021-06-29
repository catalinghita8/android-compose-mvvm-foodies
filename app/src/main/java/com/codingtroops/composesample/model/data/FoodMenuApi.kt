package com.codingtroops.composesample.model.data

import com.codingtroops.composesample.model.response.FoodCategoriesResponse
import com.codingtroops.composesample.model.response.MealsResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodMenuApi @Inject constructor(private val service: Service) {

    suspend fun getFoodCategories(): FoodCategoriesResponse = service.getFoodCategories()
    suspend fun getMealsByCategory(categoryId: String): MealsResponse =
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


