package com.codingtroops.composesample.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val LAUNCH_LISTEN_TO_EFFECTS = "launch-listen-to-effects"

abstract class ViewState(var isLoading: Boolean = false) {
    fun <State : ViewState> State.setIsLoading(value: Boolean): State {
        isLoading = value
        return this
    }
}

interface ViewEvent

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, State : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    private val initialState: State by lazy { setInitialState() }

    abstract fun setInitialState(): State

    private val currentState: State
        get() = viewState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val viewState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

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
            _event.collect {
                handleEvents(it)
            }
        }
    }

    abstract fun handleEvents(event: Event)

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

}