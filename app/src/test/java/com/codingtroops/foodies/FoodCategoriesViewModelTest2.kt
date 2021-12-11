package com.codingtroops.foodies

import androidx.lifecycle.SavedStateHandle
import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.data.*
import com.codingtroops.foodies.model.response.FoodCategoriesResponse
import com.codingtroops.foodies.model.response.FoodCategoryResponse
import com.codingtroops.foodies.model.response.MealsResponse
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FoodCategoriesViewModelTest2 {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun testSuccessState() = scope.runTest {
        val viewModel = constructSUT {
            return@constructSUT TestConstants.foodCategoriesResponse
        }

        // Test initial state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            FoodCategoriesContract.State(
                categories = emptyList(),
                isLoading = true,
                error = null
            )
        )
        advanceUntilIdle()

        // Test success state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            FoodCategoriesContract.State(
                categories = TestConstants.expectedItems,
                isLoading = false
            )
        )
    }

    @Test
    fun testError() = scope.runTest {
        val viewModel = constructSUT {
            throw Exception(TestConstants.errorMessage)
        }
        advanceUntilIdle()

        // Test error state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            FoodCategoriesContract.State(
                categories = emptyList(),
                isLoading = false,
                error = TestConstants.errorMessage
            )
        )
    }

    private fun constructSUT(content: () -> FoodCategoriesResponse): FoodCategoriesViewModel {
        val fakeAPI = FakeItemsAPI()
        fakeAPI.setContentToReturn { content() }
        return FoodCategoriesViewModel(
            GetFoodItemsUSeCase(FoodMenuRepository(fakeAPI, FakeFoodDao(), dispatcher)),
            SavedStateHandle(),
            dispatcher
        )
    }

}

class FakeItemsAPI : IFoodMenuApi {
    private lateinit var content: () -> FoodCategoriesResponse

    fun setContentToReturn(content: () -> FoodCategoriesResponse) {
        this.content = content
    }

    override suspend fun getFoodCategories(): FoodCategoriesResponse {
        delay(1000)
        return content()
    }

    override suspend fun getMealsByCategory(categoryId: String): MealsResponse {
        TODO("Not yet implemented")
    }
}

class FakeFoodDao : FoodDao {
    private var items = listOf<FoodItem>()
    override suspend fun getAll() = items

    override suspend fun addAll(newItems: List<FoodItem>) {
        items = (items + newItems).distinctBy { it.id }
    }
}

object TestConstants {
    private val mockItems = listOf(
        FoodCategoryResponse(
            "id",
            "name",
            "thumbURL",
            "description"
        )
    )
    val expectedItems = listOf(
        FoodItem(
            "id",
            "name",
            "thumbURL",
            "description"
        )
    )

    val foodCategoriesResponse = FoodCategoriesResponse(mockItems)

    const val errorMessage = "Retrieval failed"
}