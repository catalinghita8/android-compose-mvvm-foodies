package com.codingtroops.composesample.di

import com.codingtroops.composesample.ui.feature.category_details.FoodCategoryDetailsViewModel

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ViewModelAssistedFactory {
    fun create(categoryId: String): FoodCategoryDetailsViewModel
}