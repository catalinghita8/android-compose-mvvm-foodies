package com.codingtroops.composesample.feature.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codingtroops.composesample.feature.food.FoodCategoriesScreen
import com.codingtroops.composesample.feature.food.FoodCategoriesViewModel
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme

// Single Activity per app
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                FoodCategoriesScreen(viewModel = FoodCategoriesViewModel())
            }
        }
    }
}