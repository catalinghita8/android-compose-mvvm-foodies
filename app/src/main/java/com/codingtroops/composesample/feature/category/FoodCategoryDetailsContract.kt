package com.codingtroops.composesample.feature.category

import com.codingtroops.composesample.base.ViewEvent
import com.codingtroops.composesample.base.ViewSideEffect
import com.codingtroops.composesample.base.ViewState
import com.codingtroops.composesample.model.FoodItem
import com.codingtroops.composesample.model.response.FoodCategoryResponse


class FoodCategoryDetailsContract {
    sealed class Event : ViewEvent

    data class State(
        val category: FoodItem?,
        val categoryFoodItems: List<FoodItem>
        ) : ViewState()

    sealed class Effect : ViewSideEffect

}