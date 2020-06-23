package com.sample

actual class KStore<TState:Any>() {
    actual val state: TState
        get() = TODO("Android comes later")

    actual fun dispatch(action: Action) {
        TODO("Android comes later")
    }
}