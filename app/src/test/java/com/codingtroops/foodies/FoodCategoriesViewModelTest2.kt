package com.codingtroops.foodies

import androidx.lifecycle.SavedStateHandle
import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.data.FoodMenuRepositoryContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class FoodCategoriesViewModelTest2 {

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun testSuccessState() = scope.runTest {
        val viewModel = FoodCategoriesViewModel(
            FakeRestaurantsRepository { TestConstants.restaurants },
            SavedStateHandle(),
            dispatcher
        )
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
                categories = TestConstants.restaurants,
                isLoading = false
            )
        )
    }
//
//    @Test
//    fun testError() = runTest {
//        val viewModel = FoodCategoriesViewModel(
//            FakeRestaurantsRepository { throw Throwable(TestConstants.repoError) },
//            SavedStateHandle()
//        )
//        advanceUntilIdle()
//
//        // Test error state
//        Truth.assertThat(viewModel.state.value).isEqualTo(
//            FoodCategoriesContract.State(
//                categories = emptyList(),
//                isLoading = false,
//                error = TestConstants.repoError
//            )
//        )
//    }
}

class FakeRestaurantsRepository(private val content: () -> List<FoodItem>) : FoodMenuRepositoryContract {
    override suspend fun getFoodCategories(): List<FoodItem> {
        delay(300)
        return content()
    }
}

object TestConstants {
    val restaurants = listOf(
        FoodItem(
            "id",
            "name",
            "thumbURL",
            "description"
        )
    )

    const val repoError = "Retrieval failed"
}