package com.sample

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.selects.select

actual class KStore<TState:Any>(initialState: TState, reducer: Reducer<TState>) {
    private val actions = Channel<Action>()
    private val states = Channel<TState>()

    init {
        storeBuilder(initialState, actions, states, reducer)
    }

    private fun <TState> storeBuilder(
        initial: TState,
        actions: ReceiveChannel<Action>,
        stateChanges: Channel<TState>,
        reducer: Reducer<TState>) = runBlocking {
        GlobalScope.launch {
            val stateFlow = MutableStateFlow(initial)
            launch {
                while (isActive) {
                    select<Unit> {
                        actions.onReceive { action ->
                            val actionFlow = when (action) {
                                is FlowAction -> action.toFlow()
                                else -> flow { emit(action) }
                            }
                            actionFlow.map {
                                reducer(stateFlow.value, it)
                            }.collect {
                                stateFlow.value = it
                            }
                        }
                    }
                }
            }
            stateFlow.collect {
                stateChanges.send(it)
            }
        }
    }

    actual fun dispatch(action: Action) = runBlocking {
        actions.send(action)
    }

    //callback method for pushing back to swift, hopefully a better alternative can be found
    fun listen(onStateChange: (TState) -> Unit) = GlobalScope.launch {
        while (isActive) {
            select<Unit> {
                states.onReceive { value ->
                    MainScope().launch {
                        onStateChange(value)
                    }
                }
            }
        }
    }

    actual val state: TState
        get() = TODO("Not yet implemented")
}