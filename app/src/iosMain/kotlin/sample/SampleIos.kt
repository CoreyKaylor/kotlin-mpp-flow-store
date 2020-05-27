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

data class Profile(val id: Int, val name: String, val isSaving: Boolean)
data class AppState(val profile: Profile)

interface Action

interface FlowAction : Action {
    fun toFlow() : Flow<Action>
}

typealias Reducer<TState> = (TState, Action) -> TState

@OptIn(ExperimentalCoroutinesApi::class)
open class KStore<TState:Any>(initialState: TState, private val reducer: Reducer<TState>) {
    private val state = MutableStateFlow(initialState)
    val currentState: StateFlow<TState> = state

    fun dispatch(action: Action) {
        runBlocking {
            when (action) {
                is FlowAction -> runFlow(action.toFlow())
                else -> runFlow(flow {
                    emit(action)
                })
            }

        }
    }

    private suspend fun runFlow(flow: Flow<Action>) {
        flow.buffer().collect {
            var current = state.value
            var newState = reducer(current, it)
            println("Setting state")
            state.value = newState
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

fun appReducer(state: AppState, action: Action): AppState {
    return when(action) {
        is ProfileAction -> state.copy(profile = profileReducer(state.profile, action))
        else -> {
            println("Unrecognized action")
            return state
        }
    }
}

private fun profileReducer(profile: Profile, action: ProfileAction): Profile {
    return when (action) {
        is ProfileAction.ProfileCreated -> profile.copy(id = profile.id + 1,
            name = action.name, isSaving = false)
        is ProfileAction.SavingProfile -> profile.copy(isSaving = true)
        is ProfileAction.SavingProfileError -> {
            println(action.ex)
            return profile.copy(isSaving = false)
        }
    }
}

sealed class ProfileAction : Action {
    internal class SavingProfile : ProfileAction()
    internal class ProfileCreated(val name: String) : ProfileAction()
    internal class SavingProfileError(val ex: Exception) : ProfileAction()

    class Create(val name: String) : FlowAction {
        override fun toFlow() = flow {
            try {
                println("saving profile")
                emit(SavingProfile())
                delay(3000)
                emit(ProfileCreated(name))
                println("profile created success")
            } catch(ex: Exception) {
                emit(SavingProfileError(ex))
            }
        }.flowOn(Dispatchers.Default)
    }
}
