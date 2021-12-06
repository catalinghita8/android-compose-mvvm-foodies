package com.codingtroops.foodies.di

import com.codingtroops.foodies.model.data.*
import com.codingtroops.foodies.model.data.FoodMenuApi.Companion.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FoodMenuApiProvider {

    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodMenuRepository(foodMenuApi: IFoodMenuApi, @IoDispatcher dispatcher: CoroutineDispatcher): FoodMenuRepository{
        return FoodMenuRepository(foodMenuApi, dispatcher)
    }

    @Provides
    @Singleton
    fun provideFoodMenuUseCase(repo: FoodMenuRepository) : GetFoodItemsUSeCase {
        return GetFoodItemsUSeCase(repo)
    }

    @Provides
    @Singleton
    fun provideFoodMenuApi(service: FoodMenuApi.Service, @IoDispatcher dispatcher: CoroutineDispatcher): IFoodMenuApi {
        return FoodMenuApi(service, dispatcher)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodMenuApiService(
        retrofit: Retrofit
    ): FoodMenuApi.Service {
        return retrofit.create(FoodMenuApi.Service::class.java)
    }

}