package com.example.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Suppress("LeakingThis")
abstract class BaseViewModel<Event, State, SideEffect> : ViewModel() {

    private val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()

    var sideEffectFlow: Channel<SideEffect> = Channel(Channel.CONFLATED)
        private set

    protected abstract fun initState(): State
    abstract fun onLoading(loading: Boolean)
    abstract fun onEvent(event: Event)
    abstract fun sendSideEffect(sideEffect: SideEffect)

    protected fun updateState(newState: (currentState: State) -> State) {
        _state.update { state: State ->
            newState(state)
        }
    }
}