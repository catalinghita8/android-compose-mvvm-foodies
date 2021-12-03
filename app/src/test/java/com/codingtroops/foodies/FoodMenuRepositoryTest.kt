package com.codingtroops.foodies

import androidx.lifecycle.SavedStateHandle
import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.data.FoodMenuRepository
import com.codingtroops.foodies.model.data.IFoodMenuApi
import com.codingtroops.foodies.model.response.FoodCategoriesResponse
import com.codingtroops.foodies.model.response.FoodCategoryResponse
import com.codingtroops.foodies.model.response.MealsResponse
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
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
        val repo = FoodMenuRepository(FakeApi(), dispatcher)
        Truth.assertThat(repo.getFoodCategories()).isEqualTo(
            foodItem
        )
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
val foodItem = listOf(FoodItem("s", "", "", ""))