package com.codingtroops.foodies.ui.feature.categories

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtroops.foodies.di.MainDispatcher
import com.codingtroops.foodies.model.data.GetFoodItemsUSeCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FoodCategoriesViewModel @Inject constructor(private val getFoodItems: GetFoodItemsUSeCase,
                                                  private val stateHandle: SavedStateHandle,
                                                  @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val state = mutableStateOf(
        FoodCategoriesContract.State(categories = listOf(), isLoading = true, error = restoreError())
    )

    fun restoreError(): String? {
        return stateHandle["error"]
    }

    init {
        viewModelScope.launch(dispatcher) {
            try {
                getFoodCategories()
            } catch(e: Exception) {
                state.value = state.value.copy(error = e.localizedMessage, isLoading = false)
            }
        }
    }

    private suspend fun getFoodCategories() {
        val categories = getFoodItems()
        state.value = state.value.copy(categories = categories, isLoading = false, error = null)
        stateHandle["error"] = "restore_error"
    }

}
