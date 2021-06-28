package com.codingtroops.composesample.feature.category_details

import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.codingtroops.composesample.base.BaseViewModel
import com.codingtroops.composesample.model.data.FoodMenuRepository
import kotlinx.coroutines.launch


class FoodCategoryDetailsViewModel(
    private val categoryId: String,
    private val repository: FoodMenuRepository
) : BaseViewModel<
        FoodCategoryDetailsContract.Event,
        FoodCategoryDetailsContract.State,
        FoodCategoryDetailsContract.Effect>() {
    init {
        viewModelScope.launch {
            val categories = repository.getFoodCategories()
            val category = categories.first { it.id == categoryId }
            setState { copy(category = category) }

            val foodItems = repository.getMealsByCategory(categoryId)
            setState { copy(categoryFoodItems = foodItems) }
        }
    }

    override fun setInitialState() = FoodCategoryDetailsContract.State(null, listOf())
        .apply { setIsLoading(true) }

    override fun handleEvents(event: FoodCategoryDetailsContract.Event) {}
}

@Suppress("UNCHECKED_CAST")
class FoodCategoryViewModelFactory(private val param: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodCategoryDetailsViewModel(param, FoodMenuRepository.instance) as T
    }

}