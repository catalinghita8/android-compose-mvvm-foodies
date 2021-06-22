package com.codingtroops.composesample.feature.food

import com.codingtroops.composesample.base.ViewSideEffect
import com.codingtroops.composesample.base.ViewEvent
import com.codingtroops.composesample.base.ViewState
import com.codingtroops.composesample.model.response.FoodCategory

class FoodCategoriesContract {
    sealed class Event : ViewEvent {
        data class CategorySelection(val categoryName: String) : Event()
    }

    data class FoodMenuState(
        val categories: List<FoodCategory> = listOf()
    ) : ViewState()

    sealed class Effect : ViewSideEffect {
        data class CategoryDetailsNavigation(val categoryName: String) : Effect()
    }

}