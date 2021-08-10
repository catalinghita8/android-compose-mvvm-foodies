package com.codingtroops.foodies

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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.whenever
import org.mockito.runners.MockitoJUnitRunner
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FoodCategoriesViewModelTest {
    @Mock
    lateinit var mockRepo: FoodMenuRepositoryContract

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    @Throws(Exception::class)
    fun setup() {
        // init mocks
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }


    @After
    fun tearDown() {
        // 2
        Dispatchers.resetMain()
        // 3
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
//            mockRepo.stub {
//                onBlocking { getFoodCategories() }.doReturn(dummyList)
//            }
            val mockedRepo = mockk<FoodMenuRepositoryContract>()
            coEvery {
                mockedRepo.getFoodCategories()
            } returns dummyList
            val viewModel = FoodCategoriesViewModel(mockedRepo)

//            whenever(dummyRepo.getFoodCategories()).thenReturn(dummyList)

            assertThat(viewModel.state.value).isEqualTo(
                FoodCategoriesContract.State(
                    categories = dummyList,
                    isLoading = false
                )
            )
        }
}