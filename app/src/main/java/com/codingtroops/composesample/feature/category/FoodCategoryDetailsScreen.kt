package com.codingtroops.composesample.feature.category

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme
import androidx.compose.ui.unit.max
import coil.transform.CircleCropTransformation
import com.codingtroops.composesample.feature.food.FoodItemDetails
import com.codingtroops.composesample.feature.food.FoodItemRow
import com.codingtroops.composesample.model.FoodItem
import com.google.accompanist.coil.rememberCoilPainter
import kotlin.math.min


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FoodCategoryDetailsScreen(state: FoodCategoryDetailsContract.State) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

    Surface(color = MaterialTheme.colors.background) {
        Column {
            CategoryDetails(state.category, scrollOffset)
            LazyColumn(state = scrollState) {
                items(state.categoryFoodItems) { item ->
                    FoodItemRow(item = item,
                        iconTransformationBuilder = { transformations(CircleCropTransformation()) })
                }
            }
        }
    }

}

@Composable
private fun CategoryDetails(
    category: FoodItem?,
    scrollOffset: Float,
) {
    Row {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = Color.Black
            ),
            elevation = 4.dp
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = category?.thumbnailUrl,
                    requestBuilder = {
                        transformations(CircleCropTransformation())
                    },
                ),
                modifier = Modifier.size(max(64.dp, 128.dp * scrollOffset)),
                contentDescription = "Food category thumbnail picture",
            )
        }
        FoodItemDetails(
            item = category,
            expandedLines = (kotlin.math.max(2f, scrollOffset * 5)).toInt(),
            modifier = Modifier
                .padding(
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun FoodCategoryDetailsDefaultPreview() {
    ComposeSampleTheme {
        FoodCategoryDetailsScreen(viewModel(factory = FoodCategoryViewModelFactory("Lamb")))
    }
}