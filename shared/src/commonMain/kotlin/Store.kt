package com.sample

import kotlinx.coroutines.flow.Flow

interface Action

interface FlowAction : Action {
    fun toFlow() : Flow<Action>
}

typealias Reducer<TState> = (TState, Action) -> TState

expect class KStore<TState:Any> {
    val state: TState
    fun dispatch(action: Action)
}