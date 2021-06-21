package com.codingtroops.composesample.feature.food

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingtroops.composesample.feature.entry.AppNavigationController
import com.codingtroops.composesample.model.response.FoodCategory
import com.codingtroops.composesample.noRippleClickable
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun FoodCategoriesScreen(
    viewModel: FoodCategoriesViewModel,
    navigationController: AppNavigationController
) {
    Surface(color = MaterialTheme.colors.background) {
        val state = viewModel.viewState.collectAsState().value
        if (state.isLoading)
            LoadingBar()
        FoodCategories(state.categories, navigationController)
    }
}

@Composable
fun FoodCategories(categories: List<FoodCategory>, navigationController: AppNavigationController) {
    LazyColumn {
        items(categories) { category ->
            FoodCategoryRow(category, navigationController)
        }
    }
}

@Composable
fun FoodCategoryRow(category: FoodCategory, navigationController: AppNavigationController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable {
                navigationController.navigateToCategoryDetails(category.name)
            }
    ) {
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.animateContentSize(),
        ) {
            Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                FoodCategoryThumbnail(category.thumbnailUrl)
            }
            FoodCategoryDetails(category, expanded)
            Box(
                modifier = Modifier
                    .align(
                        if (expanded)
                            Alignment.Bottom
                        else
                            Alignment.CenterVertically
                    )
                    .noRippleClickable { expanded = !expanded }
            ) {
                ExpandableContentIcon(expanded)
            }
        }
    }
}

@Composable
private fun ExpandableContentIcon(expanded: Boolean) {
    Icon(
        imageVector = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown,
        contentDescription = "Expand row icon",
        modifier = Modifier
            .padding(all = 16.dp)
    )
}

@Composable
private fun FoodCategoryDetails(
    category: FoodCategory,
    expanded: Boolean
) {
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 8.dp,
                top = 24.dp,
                bottom = 24.dp
            )
            .fillMaxWidth(0.80f)
    ) {
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
                maxLines = if (expanded) 10 else 2
            )
        }
    }
}

@Composable
fun FoodCategoryThumbnail(thumbnailUrl: String) {
    Image(
        painter = rememberCoilPainter(
            request = thumbnailUrl
        ),
        modifier = Modifier
            .size(88.dp)
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        contentDescription = "Food category thumbnail picture",
    )
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
    val appNavigationController = AppNavigationController(null)
    ComposeSampleTheme {
        FoodCategoriesScreen(FoodCategoriesViewModel(), appNavigationController)
    }
}

const val ONE_TIME_EFFECT_GET_FOOD_CATEGORIES = "get-categories"