package com.codingtroops.composesample.feature.category

import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class FoodCategoryDetailsViewModel(private val categoryName: String) : ViewModel() {
}

class FoodCategoryViewModelFactory(private val param: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodCategoryDetailsViewModel(param) as T
    }

}