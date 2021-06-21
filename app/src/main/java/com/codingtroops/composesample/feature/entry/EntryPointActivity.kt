package com.codingtroops.composesample.feature.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.codingtroops.composesample.feature.category.FoodCategoryViewModelFactory
import com.codingtroops.composesample.feature.entry.NavigationKeys.Arg.FOOD_CATEGORY_NAME
import com.codingtroops.composesample.feature.food.FoodCategoriesScreen
import com.codingtroops.composesample.feature.food.FoodCategoriesViewModel
import com.codingtroops.composesample.feature.food.FoodCategoryDetailsScreen
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
    val appNavigationController = AppNavigationController(navController)
    NavHost(navController, startDestination = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
        composable(route = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
            FoodCategoriesScreen(
                viewModel = viewModel(),
                navigationController = appNavigationController
            )
        }
        composable(
            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.FOOD_CATEGORY_NAME) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoryName =
                backStackEntry.arguments!!.getString(NavigationKeys.Arg.FOOD_CATEGORY_NAME)!!
            FoodCategoryDetailsScreen(
                viewModel(factory = FoodCategoryViewModelFactory(categoryName)), categoryName
            )
        }
    }
}

class AppNavigationController(private val navController: NavController?) : NavigationContract {
    override fun navigateToCategoryDetails(category: String) {
        navController?.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/$category")
    }
}

interface NavigationContract {
    fun navigateToCategoryDetails(category: String)
}

object NavigationKeys {

    object Arg {
        const val FOOD_CATEGORY_NAME = "foodCategoryName"
    }

    object Route {
        const val FOOD_CATEGORIES_LIST = "food_categories_list"
        const val FOOD_CATEGORY_DETAILS = "$FOOD_CATEGORIES_LIST/{$FOOD_CATEGORY_NAME}"
    }

}