package com.sample

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface Action

interface FlowAction : Action {
    fun toFlow() : Flow<Action>
}

typealias Reducer<TState> = (TState, Action) -> TState

class KStore<TState:Any>(initialState: TState, private val reducer: Reducer<TState>) {
    internal val stateFlow = MutableStateFlow(initialState)

    fun dispatch(action: Action) = GlobalScope.launch {
        val actionFlow = when (action) {
            is FlowAction -> action.toFlow()
            else -> flow<Action> { emit(action) }
        }
        actionFlow.map {
            reducer(stateFlow.value, it)
        }.collect {
            stateFlow.value = it
        }
    }

    val state: TState
        get() = stateFlow.value
}