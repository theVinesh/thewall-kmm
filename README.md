# TheWall KMM

A reusable, non-dismissable bottom sheet for Kotlin Multiplatform Mobile (Android & iOS) using Compose Multiplatform.

## Features

- ✅ **Non-dismissable** - Users must tap the CTA button to proceed
- ✅ **Cross-platform** - Works on Android and iOS via Compose Multiplatform
- ✅ **Customizable** - Full theming support with Material3 defaults
- ✅ **State Management** - Built-in support for tracking "has shown" state
- ✅ **Lightweight** - No storage dependencies (host app provides storage)

## Sample apps

- `sample-android/` is the Android host app for local demo and integration checks.
- `sample-ios/` is a checked-in SwiftUI/Xcode sample app for local iOS simulator builds.
- Both samples exercise the same two demo flows through `sample-shared/`: the onboarding wall and the closeable-wall demo.

## Quick Start

### Installation (Git Submodule)

```bash
cd your-project
git submodule add https://github.com/theVinesh/thewall-kmm libs/thewall
```

In your `settings.gradle.kts`:

```kotlin
includeBuild("libs/thewall") {
    dependencySubstitution {
        substitute(module("com.thevinesh:thewall"))
            .using(project(":thewall"))
    }
}
```

In your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.thevinesh:thewall:1.0.0")
}
```

### Usage

```kotlin
import com.thevinesh.thewall.*

// Basic usage
TheWallSheet(
    content = TheWallContent(
        title = "Welcome to MyApp",
        features = listOf(
            FeatureItem(
                icon = Icons.Default.Star,
                title = "Feature One",
                description = "Description of feature one"
            ),
            FeatureItem(
                icon = Icons.Default.Favorite,
                title = "Feature Two", 
                description = "Description of feature two"
            )
        ),
        ctaText = "Get Started"
    ),
    onCtaClicked = { /* Navigate to main app */ }
)
```

### With State Provider

For automatic "has shown" tracking:

```kotlin
// 1. Implement the state provider
class MyTheWallState(private val dataStore: DataStore<Preferences>) : TheWallStateProvider {
    override suspend fun hasBeenShown(): Boolean = 
        dataStore.data.first()[ONBOARDING_SHOWN] == true
    
    override suspend fun markAsShown() {
        dataStore.edit { it[ONBOARDING_SHOWN] = true }
    }
}

// 2. Use the state-aware composable
TheWallSheetWithState(
    stateProvider = MyTheWallState(dataStore),
    content = myTheWallContent,
    onCtaClicked = { /* Navigate */ }
)
```

## Customization

```kotlin
TheWallSheet(
    content = myContent,
    onCtaClicked = { },
    theme = TheWallTheme(
        backgroundColor = Color.White,
        cornerRadius = 32.dp,
        iconTint = MyAppColors.primary,
        contentPadding = PaddingValues(32.dp)
    )
)
```

## API Reference

### `TheWallContent`

| Property | Type | Description |
|----------|------|-------------|
| `title` | `String` | Main title (required, non-blank) |
| `features` | `List<FeatureItem>` | Feature items (can be empty) |
| `ctaText` | `String` | Button text (required, non-blank) |

### `FeatureItem`

| Property | Type | Description |
|----------|------|-------------|
| `icon` | `ImageVector` | Material icon or custom vector |
| `title` | `String` | Feature title (required, non-blank) |
| `description` | `String` | Feature description |

### `TheWallTheme`

All properties have Material3 defaults. Pass `Color.Unspecified` to use theme colors.

| Property | Default |
|----------|---------|
| `backgroundColor` | Surface color |
| `iconTint` | Primary color |
| `cornerRadius` | 28.dp |
| `contentPadding` | 24.dp all |

## Building

```bash
# Build the library
./gradlew :thewall:build

# Run library tests
./gradlew :thewall:desktopTest

# Compile the Android sample app
./gradlew :sample-android:compileDebugKotlin

# Build the Kotlin framework used by the iOS sample
./gradlew :sample-shared:linkDebugFrameworkIosSimulatorArm64
```

The iOS sample lives in `sample-ios/`. Open `sample-ios/sample-ios.xcodeproj` in Xcode and run the `sample-ios` scheme, or build it from the command line:

```bash
xcodebuild -project sample-ios/sample-ios.xcodeproj -scheme sample-ios -configuration Debug -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' build
```

The checked-in Xcode project regenerates `TheWallSampleShared.framework` from `sample-shared/` during the build, so a normal Xcode build is enough after cloning the repo.

## License

MIT License - see [LICENSE](LICENSE) for details.
