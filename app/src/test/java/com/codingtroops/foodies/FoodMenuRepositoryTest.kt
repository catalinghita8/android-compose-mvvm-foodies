package com.codingtroops.foodies

import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.data.FoodMenuRepository
import com.codingtroops.foodies.model.data.IFoodMenuApi
import com.codingtroops.foodies.model.response.FoodCategoriesResponse
import com.codingtroops.foodies.model.response.FoodCategoryResponse
import com.codingtroops.foodies.model.response.MealsResponse
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FoodMenuRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun testSuccessState() = scope.runTest {
        val fakeDao = FakeFoodDao()
        val repo = FoodMenuRepository(FakeApi(), fakeDao, dispatcher)
        Truth.assertThat(repo.getFoodCategories()).isEqualTo(
            foodItems
        )
        Truth.assertThat(fakeDao.getAll()).isEqualTo(foodItems)
    }
}
class FakeApi: IFoodMenuApi {
    override suspend fun getFoodCategories(): FoodCategoriesResponse {
        delay(1000)
        return dummyResponse
    }

    override suspend fun getMealsByCategory(categoryId: String): MealsResponse {
        TODO("Not yet implemented")
    }
}

val dummyResponse = FoodCategoriesResponse(listOf(FoodCategoryResponse("s", "", "", "")))
val foodItems = listOf(FoodItem("s", "", "", ""))
