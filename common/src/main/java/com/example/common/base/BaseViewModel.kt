package com.example.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Suppress("LeakingThis")
abstract class BaseViewModel<Event, State> : ViewModel() {

    private val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()

    protected abstract fun initState(): State
    abstract fun onLoading(loading: Boolean)
    abstract fun onEvent(event: Event)

    protected fun updateState(newState: (currentState: State) -> State) {
        _state.update { state: State ->
            newState(state)
        }
    }
}