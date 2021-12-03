package com.codingtroops.foodies

import androidx.lifecycle.SavedStateHandle
import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.model.data.FoodMenuRepository
import com.codingtroops.foodies.model.data.FoodMenuRepositoryContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class FoodCategoriesViewModelTest {

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testListIsPassedToState() =
        runTest {
            val dummyList = listOf(
                FoodItem(
                    "id",
                    "name",
                    "thumbURL",
                    "description"
                )
            )
//            coEvery {
//                mockedRepo.getFoodCategories()
//            } coAnswers {
//                delay(300)
//                dummyList
//            }
//
//            val viewModel = FoodCategoriesViewModel(mockedRepo, SavedStateHandle())
//            // Test initial state
//            assertThat(viewModel.state.value).isEqualTo(
//                FoodCategoriesContract.State(
//                    categories = emptyList(),
//                    isLoading = true,
//                    error = null
//                )
//            )
            advanceUntilIdle()

//            // Test success state
//            assertThat(viewModel.state.value).isEqualTo(
//                FoodCategoriesContract.State(
//                    categories = dummyList,
//                    isLoading = false
//                )
//            )
//            val newStoredValue: String? = savedStateHandle["error"]
//            assertThat(newStoredValue).isEqualTo("restore_error")
        }
}