# NavEntryScope library and annotation processor

[![CI](https://github.com/mercari/sample-nav-entry-scope-android/actions/workflows/ci.yml/badge.svg)](https://github.com/mercari/sample-nav-entry-scope-android/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.txt)
![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg)

This project offers an implementation for the **NavEntryScope** pattern used for sharing dependencies across multiple ViewModels on the same screen, along with a sample app.

To learn more, check out the droidcon Italy Slides: [Slide Deck](https://bit.ly/NavEntryScopeSlides)

## What is NavEntryScope?

In single-activity Android apps with Compose navigation, screens often have multiple ViewModels that need to share data. The existing Hilt scopes don't fit this use case well:

- `@Singleton` / `@ActivityRetainedScoped`: Too broad because shared across all screens, causing state leakage between multiple instances of the same screen in the back stack
- `@ViewModelScoped`: Too narrow because each ViewModel gets its own instance, making sharing impossible

**NavEntryScope** is a custom Hilt scope that provides screen-level scoping. Dependencies annotated with `@NavEntryScoped` are:
- Shared across all ViewModels on the same screen
- Isolated per navigation entry: each screen instance gets its own dependencies
- Automatically cleaned up when the screen is removed from the back stack

## When to Use NavEntryScope

Use NavEntryScope when you have:
- Multiple ViewModels on the same screen that depend on shared data
- Complex screens where a single ViewModel would become too large
- Reusable UI components with their own ViewModels that need access to screen-level data
- Multiple instances of the same screen in the navigation back stack

## How to Use

### 1. Annotate Shared Dependencies

```kotlin
@Module
@InstallIn(NavEntryComponent::class)
interface YourModule {

    @Binds
    @NavEntryScoped
    fun bindRepository(impl: YourRepositoryImpl): YourRepository
}
```

### 2. Inject into ViewModels

ViewModels remain standard `@HiltViewModel` classes. Injecting a dependency that depends on `@NavEntryScoped` into your ViewModel is also supported.

```kotlin
@HiltViewModel
class YourViewModel @Inject constructor(
    private val repository: YourRepository  // NavEntryScoped
) : ViewModel()
```

### 3. Use in Compose

Replace `hiltViewModel()` with `navEntryScopedViewModel()`:

```kotlin
@Composable
fun YourScreen() {
    val viewModel: YourViewModel = navEntryScopedViewModel()
    // ...
}
```

The annotation processor automatically generates the necessary Dagger modules (`NavEntry_Module`) and entry points (`NavEntry_EntryPoint`) to bridge `NavEntryComponent` and `ViewModelComponent`.

## Project Structure

### [`app`](app/)
Sample application demonstrating NavEntryScope usage with:
- `SharedCounterRepository` - A `@NavEntryScoped` repository holding the shared value
- `CounterLabelViewModel` - Displays the counter value
- `IncrementButtonViewModel` - Increments the counter

Both ViewModels access the same repository instance, demonstrating data sharing on a single screen.

### [`nav-entry-scope/lib`](nav-entry-scope/lib/)
Core library providing:
- `@NavEntryScoped` - Scope annotation for navigation entry lifetime
- `NavEntryComponent` - Custom Dagger component definition
- `NavEntryComponentOwner` - ViewModel managing component lifecycle
- `navEntryScopedViewModel()` - Compose function for injecting ViewModels with NavEntryScoped dependencies

### [`nav-entry-scope/processor`](nav-entry-scope/processor/)
Annotation processor (KSP) that generates:
- `NavEntry_EntryPoint` - Hilt EntryPoint interface installed in `NavEntryComponent`
- `NavEntry_Module` - Dagger module installed in `ViewModelComponent` that provides NavEntryScoped dependencies

## Contribution

If you want to submit a PR for bug fixes or documentation, please read the [CONTRIBUTING.md](CONTRIBUTING.md) and follow the instruction beforehand.

## License

nav-entry-scope-android is released under the [MIT License](LICENSE.txt).
