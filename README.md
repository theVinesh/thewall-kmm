# TheWall KMM

TheWall is a Compose Multiplatform bottom sheet for product education and conversion moments. Use it when you want one reusable wall across Android and iOS for onboarding, feature highlights, paywall-style messaging, upgrade prompts, and announcements.

It ships as UI only: you provide the content, CTA behavior, and any persistence for “has this already been shown?”.

## Overview

- Compose Multiplatform UI for Android and iOS
- Title + feature list + single CTA flow
- Optional close button when the wall should be dismissible
- Host-owned shown-state storage through `TheWallStateProvider`
- Material3-friendly theming via `TheWallTheme`

## Supported platforms and sample apps

### Library targets in this repo

- Android (`androidTarget`)
- iOS frameworks for `iosX64`, `iosArm64`, and `iosSimulatorArm64`
- A desktop JVM target used in this repo for tests/verification

### Sample apps in this repo

- `sample-shared/` contains the shared demo UI and state flow
- `sample-android/` is the Android host app; `MainActivity` renders `TheWallSampleApp()`
- `sample-ios/` is the SwiftUI/Xcode host app; it embeds `TheWallSampleShared.framework`, which is built from `sample-shared/`

### Demo flows currently shown

- Onboarding wall
- Closeable wall demo

## Supported integration path

The repo-backed integration path today is **source/project dependency integration inside a Gradle build**.

In this repository, `sample-shared` depends on the library like this:

```kotlin
dependencies {
    implementation(project(":thewall"))
}
```

If you want to use TheWall in your own app, the supported path represented by this repo is:

1. Make the `thewall/` module part of your Gradle build.
2. Add `:thewall` to `settings.gradle.kts`.
3. Depend on it from your shared UI module with `implementation(project(":thewall"))`.
4. If you have an iOS host app, expose your shared module to Xcode the same way this repo does with `sample-shared`.

### Not currently provided by this repo

This repository does **not** currently demonstrate or ship:

- a published Maven artifact
- CocoaPods distribution
- Swift Package Manager distribution
- XCFramework packaging
- a direct Swift integration path against a packaged `TheWall.framework`

The checked-in iOS sample is a SwiftUI host around `sample-shared`, not a direct consumer of `TheWall` from Swift.

## Quick start

### Create content

Examples below use Material icons from `compose.material-icons-extended`, matching the sample app.

```kotlin
val onboardingWall = TheWallContent(
    title = "Welcome to the App",
    features = listOf(
        FeatureItem(Icons.Default.Widgets, "Beautiful Widgets", "Track your stats from your home screen"),
        FeatureItem(Icons.Default.Refresh, "Auto Updates", "Keep your data fresh automatically"),
        FeatureItem(Icons.Default.Notifications, "Smart Alerts", "Stay on top of important changes"),
    ),
    ctaText = "Get Started",
)
```

### Show a required wall

Use `TheWallSheet` when the user must complete the CTA to continue.

```kotlin
TheWallSheet(
    content = onboardingWall,
    onCtaClicked = onContinue,
)
```

### Show a closeable wall

Pass `onClose` when the wall should render the optional close button.

```kotlin
TheWallSheet(
    content = announcementWall,
    onCtaClicked = onOpenReleaseNotes,
    onClose = onDismissAnnouncement,
)
```

### Auto-show once with `TheWallSheetWithState`

Use `TheWallSheetWithState` when you want the wall to appear only if your app says it has not been shown yet.

```kotlin
class OnboardingWallState(
    private val dataStore: DataStore<Preferences>,
) : TheWallStateProvider {
    override suspend fun hasBeenShown(): Boolean = dataStore.data.first()[ONBOARDING_SHOWN] == true

    override suspend fun markAsShown() {
        dataStore.edit { it[ONBOARDING_SHOWN] = true }
    }
}

TheWallSheetWithState(
    stateProvider = OnboardingWallState(dataStore),
    content = onboardingWall,
    onCtaClicked = onContinue,
)
```

`TheWallSheetWithState` is the state-aware convenience wrapper for CTA-driven flows. If you need optional close behavior, use `TheWallSheet` directly and manage visibility in your own state.

## API reference

### `TheWallSheet`

Primary composable for rendering the wall.

| Parameter | Type | Notes |
| --- | --- | --- |
| `content` | `TheWallContent` | Title, features, and CTA label |
| `onCtaClicked` | `() -> Unit` | Called after the CTA dismissal flow completes |
| `onClose` | `(() -> Unit)? = null` | Optional; when provided, shows the close button |
| `modifier` | `Modifier = Modifier` | Applied to the sheet container |
| `theme` | `TheWallTheme = TheWallTheme()` | Optional visual customization |

### `TheWallSheetWithState`

Convenience composable that checks `TheWallStateProvider.hasBeenShown()` before rendering and calls `markAsShown()` when the CTA is pressed.

| Parameter | Type | Notes |
| --- | --- | --- |
| `stateProvider` | `TheWallStateProvider` | Host-provided shown-state implementation |
| `content` | `TheWallContent` | Wall copy and feature items |
| `onCtaClicked` | `() -> Unit` | Called after `markAsShown()` completes |
| `theme` | `TheWallTheme = TheWallTheme()` | Optional visual customization |

### `TheWallContent`

Data model for the wall body.

| Property | Type | Notes |
| --- | --- | --- |
| `title` | `String` | Required; must not be blank |
| `features` | `List<FeatureItem>` | Ordered list of feature rows |
| `ctaText` | `String` | Required; must not be blank |

### `FeatureItem`

One row inside the wall feature list.

| Property | Type | Notes |
| --- | --- | --- |
| `icon` | `ImageVector` | Material icon or custom vector |
| `title` | `String` | Required; must not be blank |
| `description` | `String` | Supporting copy for the feature |

### `TheWallTheme`

All properties have sensible defaults. Leave `Color.Unspecified`, `TextStyle.Default`, or `null` values in place to fall back to Material3 behavior.

| Property | Type | Purpose |
| --- | --- | --- |
| `backgroundColor` | `Color` | Sheet background color |
| `titleStyle` | `TextStyle` | Title typography override |
| `featureTitleStyle` | `TextStyle` | Feature title typography override |
| `featureDescriptionStyle` | `TextStyle` | Feature description typography override |
| `ctaButtonColors` | `ButtonColors?` | CTA button color override |
| `iconTint` | `Color` | Feature icon tint override |
| `cornerRadius` | `Dp` | Top corner radius of the sheet |
| `contentPadding` | `PaddingValues` | Padding around the sheet content |

### `TheWallStateProvider`

Host-owned interface for shown-state persistence.

| Function | Returns | Purpose |
| --- | --- | --- |
| `hasBeenShown()` | `Boolean` | Tells the wall wrapper whether to render |
| `markAsShown()` | `Unit` | Records that the CTA-driven wall has been completed |

## Customization guidance

- **Keep behavior in app state**: TheWall handles presentation; your app decides when to show or hide it.
- **Use `TheWallTheme` for styling**: Override only the parts that need to differ from Material3 defaults.
- **Choose the right entry point**:
  - Use `TheWallSheet` for explicit host-controlled visibility and optional close support.
  - Use `TheWallSheetWithState` for “show once until CTA is completed” flows.
- **Treat copy as product messaging**: Keep titles concise, use 2–3 feature rows where possible, and make the CTA outcome obvious.

Example theme customization:

```kotlin
TheWallSheet(
    content = onboardingWall,
    onCtaClicked = onContinue,
    theme = TheWallTheme(
        backgroundColor = Color.White,
        titleStyle = MaterialTheme.typography.headlineSmall,
        featureTitleStyle = MaterialTheme.typography.titleMedium,
        featureDescriptionStyle = MaterialTheme.typography.bodyMedium,
        iconTint = BrandColors.Primary,
        cornerRadius = 32.dp,
        contentPadding = PaddingValues(32.dp),
    ),
)
```

## Practical use cases

### Onboarding

Use `TheWallSheetWithState` to show a one-time introduction when a user first lands in the app.

### What’s new

Use `TheWallSheet` for release highlights after an update. Add `onClose` when reading the update is optional.

### Paywall-style walls

Use `TheWallSheet` to explain premium value and route the CTA into your own purchase flow. TheWall does **not** include billing, entitlements, or subscription logic.

### Upgrade prompts

Use the wall to explain why a user should move to a higher tier or unlock a capability, then forward the CTA to your plan-selection flow.

### Announcement flows

Use a closeable wall for feature launches, maintenance notices, limited-time campaigns, or any message that should be prominent but dismissible.

## Working with this repo

Useful commands grounded in the checked-in modules and sample apps:

```bash
./gradlew :thewall:desktopTest
./gradlew :sample-android:compileDebugKotlin
./gradlew :sample-shared:linkDebugFrameworkIosSimulatorArm64
xcodebuild -project sample-ios/sample-ios.xcodeproj -scheme sample-ios -configuration Debug -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' build
```

The iOS Xcode project regenerates `TheWallSampleShared.framework` from `sample-shared/` during the build.

## License

MIT License — see [LICENSE](LICENSE).