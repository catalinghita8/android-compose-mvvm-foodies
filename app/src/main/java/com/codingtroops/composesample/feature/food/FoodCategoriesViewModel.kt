package com.codingtroops.composesample.feature.food

import com.codingtroops.composesample.model.data.FoodMenuRepository
import com.codingtroops.composesample.model.response.FoodCategory
import kotlinx.coroutines.flow.MutableStateFlow

class FoodCategoriesViewModel(private val repository: FoodMenuRepository = FoodMenuRepository()) {

    val viewState: MutableStateFlow<FoodMenuState> = MutableStateFlow(FoodMenuState())

    suspend fun getFoodCategories() {
        viewState.value = viewState.value.copy(isLoading = true)
        val categories = repository.getFoodCategories()
        viewState.value = viewState.value.copy(categories = categories, isLoading = false)
    }
}

data class FoodMenuState(
    val isLoading: Boolean = false,
    val categories: List<FoodCategory> = listOf()
)