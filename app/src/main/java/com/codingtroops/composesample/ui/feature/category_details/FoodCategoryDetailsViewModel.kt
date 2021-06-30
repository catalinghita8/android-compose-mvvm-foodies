package com.codingtroops.composesample.ui.feature.category_details

import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.codingtroops.composesample.base.BaseViewModel
import com.codingtroops.composesample.di.ViewModelAssistedFactory
import com.codingtroops.composesample.model.data.FoodMenuRepository
import kotlinx.coroutines.launch

/**
 * Since it requires dynamic runtime param injection, assisted injection is used.
 * @param categoryId - requires runtime dynamic injection
 */
class FoodCategoryDetailsViewModel @AssistedInject constructor(
    @Assisted private val categoryId: String,
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

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val assistedFactory: ViewModelAssistedFactory,
        private val categoryId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return assistedFactory.create(categoryId) as T
        }
    }
}
