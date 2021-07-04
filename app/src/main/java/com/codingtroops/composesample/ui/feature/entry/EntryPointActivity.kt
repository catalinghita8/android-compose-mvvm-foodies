package com.codingtroops.composesample.ui.feature.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.codingtroops.composesample.di.ViewModelAssistedFactory
import com.codingtroops.composesample.ui.feature.category_details.FoodCategoryDetailsScreen
import com.codingtroops.composesample.ui.feature.category_details.FoodCategoryDetailsViewModel
import com.codingtroops.composesample.ui.feature.categories.FoodCategoriesContract
import com.codingtroops.composesample.ui.feature.categories.FoodCategoriesScreen
import com.codingtroops.composesample.ui.feature.categories.FoodCategoriesViewModel
import com.codingtroops.composesample.ui.feature.entry.NavigationKeys.Arg.FOOD_CATEGORY_ID
import com.codingtroops.composesample.ui.theme.ComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// Single Activity per app
@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelAssistedFactory: ViewModelAssistedFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                FoodApp(viewModelAssistedFactory)
            }
        }
    }

}

@Composable
private fun FoodApp(viewModelAssistedFactory: ViewModelAssistedFactory) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
        composable(route = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
            val viewModel: FoodCategoriesViewModel = hiltViewModel()
            val state = viewModel.viewState.collectAsState().value
            FoodCategoriesScreen(
                state = state,
                effectFlow = viewModel.effect,
                onEventSent = { event -> viewModel.setEvent(event) },
                onNavigationRequested = { navigationEffect ->
                    if (navigationEffect is FoodCategoriesContract.Effect.Navigation.ToCategoryDetails) {
                        navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${navigationEffect.categoryName}")
                    }
                })
        }
        composable(
            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoryId =
                backStackEntry.arguments!!.getString(NavigationKeys.Arg.FOOD_CATEGORY_ID)!!
            val viewModel: FoodCategoryDetailsViewModel =
                viewModel(
                    factory = FoodCategoryDetailsViewModel.Factory(
                        assistedFactory = viewModelAssistedFactory,
                        categoryId = categoryId
                    )
                )
            val state = viewModel.viewState.collectAsState().value
            FoodCategoryDetailsScreen(state)
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