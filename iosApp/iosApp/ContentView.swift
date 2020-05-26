import SwiftUI
import app

struct ContentView: View {
    @EnvironmentObject var store: SwiftStore<AppState>
    
    var body: some View {
        VStack {
            Spacer()
            Text("Hello \(store.state.profile.name) with id: \(store.state.profile.id)")
            Button(action: {self.store.dispatch()}) {
                Text("Change")
            }
            Spacer()
        }.background(Color.white)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(configureStore())
    }
}
