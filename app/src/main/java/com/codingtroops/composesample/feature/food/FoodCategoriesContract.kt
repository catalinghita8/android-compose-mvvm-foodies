package com.codingtroops.composesample.feature.food

import com.codingtroops.composesample.base.ViewSideEffect
import com.codingtroops.composesample.base.ViewEvent
import com.codingtroops.composesample.base.ViewState
import com.codingtroops.composesample.model.response.FoodCategory

class FoodCategoriesContract {
    sealed class Event : ViewEvent {
        data class CategorySelection(val categoryName: String) : Event()
    }

    data class State(
        val categories: List<FoodCategory> = listOf()
    ) : ViewState()

    sealed class Effect : ViewSideEffect {
        object ToastDataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToCategoryDetails(val categoryName: String) : Navigation()
        }
    }

}