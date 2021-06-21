package com.codingtroops.composesample.feature.food

import androidx.lifecycle.viewModelScope
import com.codingtroops.composesample.base.BaseViewModel
import com.codingtroops.composesample.model.data.FoodMenuRepository
import kotlinx.coroutines.launch

class FoodCategoriesViewModel(private val repository: FoodMenuRepository = FoodMenuRepository()) :
    BaseViewModel<FoodCategoriesContract.Event, FoodCategoriesContract.FoodMenuState>() {

    init {
        viewModelScope.launch {
            getFoodCategories()
        }
    }

    override fun setInitialState() =
        FoodCategoriesContract.FoodMenuState(categories = listOf()).also { it.isLoading = true }

    override fun handleEvent(event: FoodCategoriesContract.Event) {
    }

    private suspend fun getFoodCategories() {
        setState { setIsLoading(true) }
        val categories = repository.getFoodCategories()
        setState {
            copy(categories = categories).setIsLoading(false)
        }
    }

}
