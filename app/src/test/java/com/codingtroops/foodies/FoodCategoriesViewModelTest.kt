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
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.stubbing.answers.AnswersWithDelay

@OptIn(ExperimentalCoroutinesApi::class)
class FoodCategoriesViewModelTest {
    @Mock
    lateinit var mockRepo: FoodMenuRepositoryContract

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testListIsPassedToState() =
        runBlockingTest {
            val dummyList = listOf(
                FoodItem(
                    "id",
                    "name",
                    "thumbURL",
                    "description"
                )
            )
            val mockedRepo = mockk<FoodMenuRepositoryContract>()
            coEvery {
                mockedRepo.getFoodCategories()
            } coAnswers {
                delay(300)
                dummyList
            }

            val savedStateHandle = SavedStateHandle().apply { this["error"] = "some_error" }
            val viewModel = FoodCategoriesViewModel(mockedRepo, savedStateHandle)
            // Test initial state
            assertThat(viewModel.state.value).isEqualTo(
                FoodCategoriesContract.State(
                    categories = emptyList(),
                    isLoading = true,
                    error = "some_error"
                )
            )
            testDispatcher.advanceUntilIdle()

            // Test success state
            assertThat(viewModel.state.value).isEqualTo(
                FoodCategoriesContract.State(
                    categories = dummyList,
                    isLoading = false
                )
            )
            val newStoredValue: String? = savedStateHandle["error"]
            assertThat(newStoredValue).isEqualTo("restore_error")

        }
}