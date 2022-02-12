package com.codingtroops.foodies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codingtroops.foodies.ui.feature.category_details.FoodCategoryDetailsScreen
import com.codingtroops.foodies.ui.feature.category_details.FoodCategoryDetailsViewModel
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesScreen
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesViewModel
import com.codingtroops.foodies.ui.NavigationKeys.Arg.FOOD_CATEGORY_ID
import com.codingtroops.foodies.ui.theme.ComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow


// Single Activity per app
@AndroidEntryPoint
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
            FoodCategoriesDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
                type = NavType.StringType
            })
        ) {
            FoodCategoryDetailsDestination()
        }
    }
}

@Composable
private fun FoodCategoriesDestination(navController: NavHostController) {
    val viewModel: FoodCategoriesViewModel = hiltViewModel()
    FoodCategoriesScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${itemId}")
        })
}

@Composable
private fun FoodCategoryDetailsDestination() {
    val viewModel: FoodCategoryDetailsViewModel = hiltViewModel()
    FoodCategoryDetailsScreen(viewModel.state)
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