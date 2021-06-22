package com.codingtroops.composesample.feature.category

import com.codingtroops.composesample.base.ViewEvent
import com.codingtroops.composesample.base.ViewSideEffect
import com.codingtroops.composesample.base.ViewState
import com.codingtroops.composesample.model.response.FoodCategory


class FoodCategoryDetailsContract {
    sealed class Event : ViewEvent

    data class State(
        val category: FoodCategory?,
        val categoryFoodItems: List<FoodCategory>
        ) : ViewState()

    sealed class Effect : ViewSideEffect

}