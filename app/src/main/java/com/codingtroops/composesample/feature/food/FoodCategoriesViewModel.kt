package com.codingtroops.composesample.feature.food

import androidx.lifecycle.viewModelScope
import com.codingtroops.composesample.base.BaseViewModel
import com.codingtroops.composesample.model.data.FoodMenuRepository
import kotlinx.coroutines.launch

class FoodCategoriesViewModel(private val repository: FoodMenuRepository = FoodMenuRepository()) :
    BaseViewModel<FoodCategoriesContract.Event, FoodCategoriesContract.FoodMenuState, FoodCategoriesContract.Effect>() {

    init {
        viewModelScope.launch {
            getFoodCategories()
        }
    }

    override fun setInitialState() =
        FoodCategoriesContract.FoodMenuState(categories = listOf()).apply { setIsLoading(true) }

    override fun handleEvents(event: FoodCategoriesContract.Event) {
        when (event) {
            is FoodCategoriesContract.Event.CategorySelection -> {
                setEffect { FoodCategoriesContract.Effect.CategoryDetailsNavigation(event.categoryName) }
            }
        }
    }

    private suspend fun getFoodCategories() {
        setState { setIsLoading(true) }
        val categories = repository.getFoodCategories()
        setState {
            copy(categories = categories).setIsLoading(false)
        }
    }

}
