package com.codingtroops.composesample.feature.food

import com.codingtroops.composesample.model.data.FoodMenuRepository
import com.codingtroops.composesample.model.response.FoodCategory
import kotlinx.coroutines.flow.MutableStateFlow

class FoodMenuViewModel(private val repository: FoodMenuRepository = FoodMenuRepository()) {

    val viewState: MutableStateFlow<FoodMenuState> = MutableStateFlow(FoodMenuState())

    suspend fun getFoodCategories() {
        viewState.value = viewState.value.copy(loadingStatus = true)
        val categories = repository.getFoodCategories()
        viewState.value = viewState.value.copy(categories = categories, loadingStatus = false)
    }
}

data class FoodMenuState(
    val loadingStatus: Boolean = false,
    val categories: List<FoodCategory> = listOf()
)