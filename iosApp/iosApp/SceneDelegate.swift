        import UIKit
        import SwiftUI
        import app
        
        class SceneDelegate: UIResponder, UIWindowSceneDelegate {
            
            var window: UIWindow?
            
            var store: SwiftStore<AppState> {
                return (UIApplication.shared.delegate as? AppDelegate)!.store
            }
            
            func scene(_ scene: UIScene, willConnectTo session: UISceneSession,
                       options connectionOptions: UIScene.ConnectionOptions) {
                
                let contentView = ContentView().environmentObject(store)
                if let windowScene = scene as? UIWindowScene {
                    print("Got here on startup")
                    let window = UIWindow(windowScene: windowScene)
                    window.rootViewController = UIHostingController(rootView: contentView)
                    self.window = window
                    window.makeKeyAndVisible()
                }
            }
            
            func sceneDidDisconnect(_ scene: UIScene) {
            }
            
            func sceneDidBecomeActive(_ scene: UIScene) {
            }
            
            func sceneWillResignActive(_ scene: UIScene) {
            }
            
            func sceneWillEnterForeground(_ scene: UIScene) {
            }
            
            func sceneDidEnterBackground(_ scene: UIScene) {
            }
            
        }
