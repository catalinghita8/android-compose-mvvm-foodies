package com.codingtroops.foodies.ui.feature.category_details

import com.codingtroops.foodies.base.ViewEvent
import com.codingtroops.foodies.base.ViewSideEffect
import com.codingtroops.foodies.base.ViewState
import com.codingtroops.foodies.model.FoodItem


class FoodCategoryDetailsContract {
    sealed class Event : ViewEvent

    data class State(
        val category: FoodItem?,
        val categoryFoodItems: List<FoodItem>
        ) : ViewState

    sealed class Effect : ViewSideEffect

}