package com.codingtroops.foodies.ui.feature.category_details

import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.viewModelScope
import com.codingtroops.foodies.base.BaseViewModel
import com.codingtroops.foodies.model.data.FoodMenuRepository
import com.codingtroops.foodies.ui.feature.entry.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodCategoryDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: FoodMenuRepository
) : BaseViewModel<
        FoodCategoryDetailsContract.Event,
        FoodCategoryDetailsContract.State,
        FoodCategoryDetailsContract.Effect>() {

    init {
        viewModelScope.launch {
            val categoryId = stateHandle.get<String>(NavigationKeys.Arg.FOOD_CATEGORY_ID)
                ?: throw IllegalStateException("No categoryId was passed to destination.")
            val categories = repository.getFoodCategories()
            val category = categories.first { it.id == categoryId }
            setState { copy(category = category) }

            val foodItems = repository.getMealsByCategory(categoryId)
            setState { copy(categoryFoodItems = foodItems) }
        }
    }

    override fun setInitialState() = FoodCategoryDetailsContract.State(null, listOf())

    override fun handleEvents(event: FoodCategoryDetailsContract.Event) {}

}
