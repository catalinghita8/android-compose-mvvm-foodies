package com.codingtroops.foodies.ui.feature.category_details

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.codingtroops.foodies.base.BaseViewModel
import com.codingtroops.foodies.model.data.FoodMenuRepository
import com.codingtroops.foodies.ui.feature.entry.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class FoodCategoryDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: FoodMenuRepository
) : BaseViewModel<
        FoodCategoryDetailsContract.Event,
        FoodCategoryDetailsContract.State,
        FoodCategoryDetailsContract.Effect>() {

    var timer: CustomTimer = CustomTimer(120_000, 4000)
    val timeLiveData = MutableLiveData<Long>()

    init {
        timer.start()
        timer.onTick = { millisUntilFinished ->
            timeLiveData.value = millisUntilFinished
        }
        viewModelScope.launch {
            val categoryId = stateHandle.get<String>(NavigationKeys.Arg.FOOD_CATEGORY_ID)
                ?: throw IllegalStateException("No categoryId was passed to destination.")
            val categories = repository.getFoodCategories()
            val category = categories.first { it.id == categoryId }
            setState { copy(category = category) }

            val foodItems = repository.getMealsByCategory(categoryId)
            setState { copy(categoryFoodItems = foodItems) }
        }
    }

    override fun setInitialState() = FoodCategoryDetailsContract.State(null, listOf())

    override fun handleEvents(event: FoodCategoryDetailsContract.Event) {}

    override fun onCleared() {
        timer.stop()
        super.onCleared()
    }
}

class CustomTimer(private val millisInFuture: Long, private val countDownInterval: Long): LifecycleObserver {

    private var millisUntilFinished: Long = millisInFuture
    private var timer = InternalTimer(this, millisInFuture, countDownInterval)

    private var isRunning = false
    var onTick: ((millisUntilFinished: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null

    private class InternalTimer(
        private val parent: CustomTimer,
        millisInFuture: Long,
        countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        var millisUntilFinished: Long = parent.millisUntilFinished

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }
        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        if (!isRunning && timer.millisUntilFinished > 0) {
            timer = InternalTimer(this, timer.millisUntilFinished, countDownInterval)
            timer.start()
            isRunning = true
        }
    }

    fun start() {
        timer.start()
        isRunning = true
    }

    fun stop() {
        timer.cancel()
    }
}
