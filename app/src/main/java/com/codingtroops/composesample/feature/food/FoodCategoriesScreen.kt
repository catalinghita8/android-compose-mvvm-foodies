package com.codingtroops.composesample.feature.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingtroops.composesample.model.response.FoodCategory
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun FoodCategoriesScreen(viewModel: FoodCategoriesViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        LaunchedEffect(ONE_TIME_EFFECT_GET_FOOD_CATEGORIES) {
            viewModel.getFoodCategories()
        }
        val state = viewModel.viewState.collectAsState().value
        if (state.isLoading)
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
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Row {
            Image(
                painter = rememberCoilPainter(
                    request = category.thumbnailUrl
                ),
                modifier = Modifier
                    .size(88.dp)
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    .align(alignment = Alignment.CenterVertically),
                contentDescription = "Food category thumbnail picture",
            )
            Column(modifier = Modifier.padding(start = 16.dp, end = 24.dp, top = 24.dp, bottom = 24.dp)) {
                Text(
                    text = category.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = category.description.trim(),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 2
                    )
                }
            }
        }
    }
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
        FoodCategoriesScreen(FoodCategoriesViewModel())
    }
}

const val ONE_TIME_EFFECT_GET_FOOD_CATEGORIES = "get-categories"