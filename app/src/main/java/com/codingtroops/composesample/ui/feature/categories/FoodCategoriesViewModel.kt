package com.codingtroops.composesample.ui.feature.categories

import androidx.lifecycle.viewModelScope
import com.codingtroops.composesample.base.BaseViewModel
import com.codingtroops.composesample.model.data.FoodMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodCategoriesViewModel @Inject constructor(private val repository: FoodMenuRepository) :
    BaseViewModel<FoodCategoriesContract.Event, FoodCategoriesContract.State, FoodCategoriesContract.Effect>() {

    init {
        viewModelScope.launch { getFoodCategories() }
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
