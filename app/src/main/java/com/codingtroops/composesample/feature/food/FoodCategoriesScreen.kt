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
import com.codingtroops.composesample.base.LAUNCH_LISTEN_FOR_EFFECTS
import com.codingtroops.composesample.model.response.FoodCategory
import com.codingtroops.composesample.noRippleClickable
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun FoodCategoriesScreen(
    state: FoodCategoriesContract.State,
    effectFlow: Flow<FoodCategoriesContract.Effect>?,
    onEventSent: (event: FoodCategoriesContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: FoodCategoriesContract.Effect.Navigation) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // Listen for side effects from the VM
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is FoodCategoriesContract.Effect.ToastDataWasLoaded ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Food categories are loaded.", duration = SnackbarDuration.Short
                    )
                is FoodCategoriesContract.Effect.Navigation.ToCategoryDetails -> onNavigationRequested(
                    effect
                )
            }
        }?.collect()
    }

    Scaffold(scaffoldState = scaffoldState) {
        Surface(color = MaterialTheme.colors.background) {
            if (state.isLoading)
                LoadingBar()
            FoodCategoriesList(state.categories) { itemId ->
                onEventSent(FoodCategoriesContract.Event.CategorySelection(itemId))
            }
        }
    }

}

@Composable
fun FoodCategoriesList(
    categories: List<FoodCategory>,
    onItemClicked: (id: String) -> Unit
) {
    LazyColumn {
        items(categories) { category ->
            FoodCategoryRow(category, onItemClicked)
        }
    }
}

@Composable
fun FoodCategoryRow(
    category: FoodCategory, onItemClicked: (id: String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onItemClicked(category.id) }
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
    ComposeSampleTheme {
        FoodCategoriesScreen(FoodCategoriesContract.State(), null, { }, { })
    }
}