package com.codingtroops.foodies.ui.feature.categories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtroops.foodies.base.BaseViewModel
import com.codingtroops.foodies.model.data.FoodMenuRepository
import com.codingtroops.foodies.model.data.FoodMenuRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodCategoriesViewModel @Inject constructor(private val repository: FoodMenuRepositoryContract,
                                                  private val stateHandle: SavedStateHandle
) :
    ViewModel() {

    val state = mutableStateOf(
        FoodCategoriesContract.State(categories = listOf(), isLoading = true, error = restoreError())
    )

    fun restoreError(): String? {
        return stateHandle["error"]
    }

    init {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            state.value = state.value.copy(error = throwable.localizedMessage, isLoading = false)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            getFoodCategories()
        }
    }

    private suspend fun getFoodCategories() { // 18544
        val categories = repository.getFoodCategories()
        state.value = state.value.copy(categories = categories, isLoading = false, error = null)
        stateHandle["error"] = "restore_error"
    }

}
