import com.sample.KStore
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <TState:Any> KStore<TState>.listen(onStateChange: (TState) -> Unit) {
    stateFlow.onEach {
        onStateChange(it)
    }.launchIn(MainScope())
}
