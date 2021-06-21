package com.codingtroops.composesample.feature.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtroops.composesample.model.data.FoodMenuRepository
import com.codingtroops.composesample.model.response.FoodCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FoodCategoriesViewModel(private val repository: FoodMenuRepository = FoodMenuRepository()) :
    ViewModel() {

    val viewState: MutableStateFlow<FoodMenuState> = MutableStateFlow(FoodMenuState())

    init {
        viewModelScope.launch {
            getFoodCategories()
        }
    }

    private suspend fun getFoodCategories() {
        viewState.value = viewState.value.copy(isLoading = true)
        val categories = repository.getFoodCategories()
        viewState.value = viewState.value.copy(categories = categories, isLoading = false)
    }

}

data class FoodMenuState(
    val isLoading: Boolean = false,
    val categories: List<FoodCategory> = listOf()
)