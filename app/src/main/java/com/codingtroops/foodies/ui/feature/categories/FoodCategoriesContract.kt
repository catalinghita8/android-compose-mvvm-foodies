package com.codingtroops.foodies.ui.feature.categories

import com.codingtroops.foodies.base.ViewSideEffect
import com.codingtroops.foodies.base.ViewEvent
import com.codingtroops.foodies.base.ViewState
import com.codingtroops.foodies.model.FoodItem

class FoodCategoriesContract {
    sealed class Event : ViewEvent {
        data class CategorySelection(val categoryName: String) : Event()
    }

    data class State(val categories: List<FoodItem> = listOf(), val isLoading: Boolean = false) : ViewState

    sealed class Effect : ViewSideEffect {
        object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToCategoryDetails(val categoryName: String) : Navigation()
        }
    }

}