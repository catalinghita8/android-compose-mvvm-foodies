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
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FoodCategoriesViewModelTest2 {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    @Throws(Exception::class)
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testSuccessState() = runBlocking {
        val viewModel = FoodCategoriesViewModel(
            FakeRestaurantsRepository { TestConstants.restaurants },
            SavedStateHandle()
        )
        // Test initial state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            FoodCategoriesContract.State(
                categories = emptyList(),
                isLoading = true,
                error = null
            )
        )
        testDispatcher.advanceUntilIdle()

        // Test success state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            FoodCategoriesContract.State(
                categories = TestConstants.restaurants,
                isLoading = false
            )
        )
    }

    @Test
    fun testError() = runBlocking {
        val viewModel = FoodCategoriesViewModel(
            FakeRestaurantsRepository { throw Throwable(TestConstants.repoError) },
            SavedStateHandle()
        )
        testDispatcher.advanceUntilIdle()

        // Test error state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            FoodCategoriesContract.State(
                categories = emptyList(),
                isLoading = false,
                error = TestConstants.repoError
            )
        )
    }
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