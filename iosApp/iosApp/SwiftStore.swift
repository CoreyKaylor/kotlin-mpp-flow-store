import shared

class SwiftStore<State: AnyObject> : ObservableObject {
    private var kStore: KStore<State>
    @Published var state: State

    init(initialState: State, reducer: @escaping (State, Action) -> State) {
        state = initialState
        kStore = KStore<State>(initialState: initialState, reducer: reducer)
        kStore.listen(onStateChange: { newState in
            self.state = newState as! State
        })
    }

    func dispatch(_ action: Action) {
        kStore.dispatch(action: action)
    }
}
