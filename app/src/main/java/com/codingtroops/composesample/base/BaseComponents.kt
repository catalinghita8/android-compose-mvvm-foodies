package com.codingtroops.composesample.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class ViewState(var isLoading: Boolean = false) {
    fun <State : ViewState> State.setIsLoading(value: Boolean): State {
        isLoading = value
        return this
    }
}


interface ViewEvent

abstract class BaseViewModel<Event : ViewEvent, State : ViewState> : ViewModel() {

    private val initialState: State by lazy { setInitialState() }

    abstract fun setInitialState(): State

    private val currentState: State
        get() = viewState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val viewState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val eventsFlow = _event.asSharedFlow()

    init {
        subscribeToEvents()
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: State.() -> State) {
        val newState = currentState.reducer()
        _uiState.value = newState
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            eventsFlow.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun handleEvent(event: Event)

}