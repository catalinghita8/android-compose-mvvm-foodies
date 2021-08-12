package com.codingtroops.foodies.ui.feature.category_details


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.codingtroops.foodies.ui.feature.categories.FoodItemDetails
import com.codingtroops.foodies.ui.feature.categories.FoodItemRow
import com.codingtroops.foodies.model.FoodItem
import kotlin.math.min


@Composable
fun FoodCategoryDetailsScreen(
    state: FoodCategoryDetailsContract.State,
    viewModel: FoodCategoryDetailsViewModel
) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

    val timerIsShown = remember { mutableStateOf(true) }
    val timerState = viewModel.timeLiveData.observeAsState()
    Surface(color = MaterialTheme.colors.background) {
        Column {
            Surface(elevation = 4.dp) {
                CategoryDetailsCollapsingToolbar(state.category, scrollOffset)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row {
                Button(onClick = { timerIsShown.value = !timerIsShown.value }) {
                    Text("Enable/disable timer")
                }
                TimerValue(viewModel = viewModel, timerIsShown.value, timerState.value.toString())
            }
            Spacer(modifier = Modifier.height(2.dp))
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.categoryFoodItems) { item ->
                    FoodItemRow(
                        item = item,
                        iconTransformationBuilder = {
                            transformations(
                                CircleCropTransformation()
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerValue(viewModel: FoodCategoryDetailsViewModel, shouldBeShown: Boolean, timerValue: String) {
    if (shouldBeShown)
        TimerText(viewModel, timerValue)
}

@Composable
private fun TimerText(viewModel: FoodCategoryDetailsViewModel, timerValue: String) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = viewModel) {
        viewModel.startTimer()
        lifecycle.addObserver(viewModel.timer)
        onDispose {
            viewModel.stopTimer()
            lifecycle.removeObserver(viewModel.timer)
        }
    }
    Text("Timer count is now: " + timerValue)
}

@Composable
private fun CategoryDetailsCollapsingToolbar(
    category: FoodItem?,
    scrollOffset: Float,
) {
    val imageSize by animateDpAsState(targetValue = max(72.dp, 128.dp * scrollOffset))
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
                painter = rememberImagePainter(
                    data = category?.thumbnailUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    },
                ),
                modifier = Modifier.size(max(72.dp, imageSize)),
                contentDescription = "Food category thumbnail picture",
            )
        }
        FoodItemDetails(
            item = category,
            expandedLines = (kotlin.math.max(3f, scrollOffset * 6)).toInt(),
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