import SwiftUI
import shared

struct ContentView: View {
    @EnvironmentObject var store: SwiftStore<AppState>

    var body: some View {
        VStack {
            Spacer()
            Text("Header without state")
            Text("Hello \(store.state.profile.name) with id: \(store.state.profile.id) is saving: \(store.state.profile.isSaving.description)")
            Button(action: createProfile) {
                Text("Change")
            }.disabled(store.state.profile.isSaving)
            Spacer()
        }.background(Color.white)
    }

    func createProfile() {
        store.dispatch(ProfileAction.Create(name: "Changed"))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
                .environmentObject(configureStore())
    }
}

