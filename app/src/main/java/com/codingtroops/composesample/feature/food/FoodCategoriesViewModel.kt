package com.codingtroops.composesample.feature.food

import androidx.lifecycle.viewModelScope
import com.codingtroops.composesample.base.BaseViewModel
import com.codingtroops.composesample.model.data.FoodMenuRepository
import kotlinx.coroutines.launch

class FoodCategoriesViewModel(private val repository: FoodMenuRepository = FoodMenuRepository.instance) :
    BaseViewModel<FoodCategoriesContract.Event, FoodCategoriesContract.State, FoodCategoriesContract.Effect>() {

    init {
        viewModelScope.launch {
            getFoodCategories()
        }
    }

    override fun setInitialState() =
        FoodCategoriesContract.State(categories = listOf()).apply { setIsLoading(true) }

    override fun handleEvents(event: FoodCategoriesContract.Event) {
        when (event) {
            is FoodCategoriesContract.Event.CategorySelection -> {
                setEffect { FoodCategoriesContract.Effect.Navigation.ToCategoryDetails(event.categoryName) }
            }
        }
    }

    private suspend fun getFoodCategories() {
        setState { setIsLoading(true) }
        val categories = repository.getFoodCategories()
        setState {
            copy(categories = categories).setIsLoading(false)
        }
        setEffect { FoodCategoriesContract.Effect.ToastDataWasLoaded }
    }

}
