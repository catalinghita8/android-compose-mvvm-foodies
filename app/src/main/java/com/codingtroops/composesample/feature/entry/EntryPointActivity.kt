package com.codingtroops.composesample.feature.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.codingtroops.composesample.feature.category.FoodCategoryDetailsScreen
import com.codingtroops.composesample.feature.category.FoodCategoryViewModelFactory
import com.codingtroops.composesample.feature.entry.NavigationKeys.Arg.FOOD_CATEGORY_ID
import com.codingtroops.composesample.feature.food.FoodCategoriesContract
import com.codingtroops.composesample.feature.food.FoodCategoriesScreen
import com.codingtroops.composesample.feature.food.FoodCategoriesViewModel
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme

// Single Activity per app
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                FoodApp()
            }
        }
    }

}

@Composable
private fun FoodApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
        composable(route = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
            val viewModel: FoodCategoriesViewModel = viewModel()
            val state = viewModel.viewState.collectAsState().value
            FoodCategoriesScreen(
                state = state,
            ) { event -> viewModel.setEvent(event) }

            val effect = viewModel.effect.collectAsState(null).value
            when (effect) {
                is FoodCategoriesContract.Effect.CategoryDetailsNavigation -> {
                    navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${effect.categoryName}")
                }
            }
        }
        composable(
            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments!!.getString(NavigationKeys.Arg.FOOD_CATEGORY_ID)!!
            FoodCategoryDetailsScreen(viewModel(factory = FoodCategoryViewModelFactory(categoryId)))
        }
    }
}

object NavigationKeys {

    object Arg {
        const val FOOD_CATEGORY_ID = "foodCategoryName"
    }

    object Route {
        const val FOOD_CATEGORIES_LIST = "food_categories_list"
        const val FOOD_CATEGORY_DETAILS = "$FOOD_CATEGORIES_LIST/{$FOOD_CATEGORY_ID}"
    }

}