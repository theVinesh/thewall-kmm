package com.thevinesh.thewall.sample.shared

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun TheWallSampleViewController(): UIViewController = ComposeUIViewController {
    TheWallSampleApp()
}