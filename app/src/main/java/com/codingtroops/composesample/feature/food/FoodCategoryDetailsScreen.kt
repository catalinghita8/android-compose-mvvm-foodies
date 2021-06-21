package com.codingtroops.composesample.feature.food

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.codingtroops.composesample.feature.category.FoodCategoryDetailsViewModel

@Composable
fun FoodCategoryDetailsScreen(viewModel: FoodCategoryDetailsViewModel, categoryName: String) {
    Text(categoryName)
}