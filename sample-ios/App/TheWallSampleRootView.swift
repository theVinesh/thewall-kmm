import SwiftUI
import UIKit
import TheWallSampleShared

struct TheWallSampleRootView: View {
    var body: some View {
        TheWallSampleHostView()
            .ignoresSafeArea()
    }
}

private struct TheWallSampleHostView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TheWallSampleViewControllerKt.TheWallSampleViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}