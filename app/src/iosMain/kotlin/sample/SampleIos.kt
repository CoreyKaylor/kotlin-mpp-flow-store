package sample

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

actual class Sample {
    actual fun checkMe() = 7
}

actual object Platform {
    actual val name: String = "iOS"
}

data class Profile(val id: Int, val name: String)
data class AppState(val profile: Profile)

interface Action

typealias Reducer<TState> = (TState, Action) -> TState

@OptIn(ExperimentalCoroutinesApi::class)
open class KStore<TState:Any>(initialState: TState, private val reducer: Reducer<TState>) {
    private val state = MutableStateFlow(initialState)
    val currentState: StateFlow<TState> = state

    fun dispatch(flow: Flow<Action>) {
        runBlocking {
            flow.buffer().collect {
                var current = state.value
                var newState = reducer(current, it)
                println("Setting state")
                state.value = newState
            }
        }
    }

    //callback method for pushing back to swift, hopefully a better alternative can be found
    fun listen(onStateChange: (TState) -> Unit) {
        runBlocking {
            state.onEach {
                onStateChange(it)
            }.launchIn(UIScope())
        }
    }
}

internal class UIScope: CoroutineScope {
    private val dispatcher = Dispatchers.Main
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job
}

fun defaultReducer(state: AppState, action: Action): AppState {
    val newId = state.profile.id + 1
    val profile = state.profile.copy(id = newId)
    return state.copy(profile = profile)
}

fun foo() : Flow<Action> = flow {
    println("Got here")
    emit(SimpleAction())
    delay(3000)
    emit(SimpleAction())
    println("Got here")
    delay(3000)
    emit(SimpleAction())
}.flowOn(Dispatchers.Default)

class SimpleAction : Action {

}
