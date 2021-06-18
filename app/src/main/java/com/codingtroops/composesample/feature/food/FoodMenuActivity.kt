package com.codingtroops.composesample.feature.food

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codingtroops.composesample.model.response.FoodCategory
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                FoodCategoriesScreen(viewModel = FoodMenuViewModel())
            }
        }
    }
}

@Composable
fun FoodCategoriesScreen(viewModel: FoodMenuViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        LaunchedEffect(ONE_TIME_EFFECT_GET_FOOD_CATEGORIES) {
            viewModel.getFoodCategories()
        }
        val state = viewModel.viewState.collectAsState().value
        if (state.loadingStatus)
            LoadingBar()
        FoodCategories(categories = state.categories)
    }
}

@Composable
fun FoodCategories(categories: List<FoodCategory>) {
    LazyColumn {
        items(categories) {
            FoodCategoryRow(it)
        }
    }
}

@Composable
fun FoodCategoryRow(category: FoodCategory) {
    Text(text = category.name)
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSampleTheme {
        FoodCategoriesScreen(FoodMenuViewModel())
    }
}

const val ONE_TIME_EFFECT_GET_FOOD_CATEGORIES = "get-categories"