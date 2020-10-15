import SwiftUI
import shared

@main
struct SampleApp: App {
    let store: SwiftStore<AppState>
    
    init() {
        store = configureStore()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(store)
        }
    }
}

func configureStore() -> SwiftStore<AppState> {
    SwiftStore<AppState>(initialState: AppState(profile: Profile(id: 12345, name: "Test", isSaving: false)),
                         reducer: ReducersKt.appReducer)
}
