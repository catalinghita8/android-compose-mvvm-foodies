# Foodies - Modern Android Architecture

Foodies is a sample project that presents a modern approach to Android app development.

The project tries to combine popular Android tools and to demonstrate best development practices by utilizing up to date tech-stack like Compose, Kotlin Flow and Hilt.

The sample app layers its presentation through MVVM presentation pattern. Additionally, the application features animations like expanding and collapsing row items or other effects like collapsing toolbar.

## Description

<img src="misc/demo_gif_4.gif" width="380" height="620" align="right" hspace="20">

* UI 
   * [Compose](https://developer.android.com/jetpack/compose) declarative UI framework
   * [Material design](https://material.io/design)

* Tech/Tools
    * [Kotlin](https://kotlinlang.org/) 100% coverage
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [Flow](https://developer.android.com/kotlin/flow) for async operations
    * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
    * [Jetpack](https://developer.android.com/jetpack)
        * [Compose](https://developer.android.com/jetpack/compose) 
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) for navigation between composables
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that stores, exposes and manages UI state
    * [Retrofit](https://square.github.io/retrofit/) for networking
    * [Coil](https://github.com/coil-kt/coil) for image loading
    
* Modern Architecture
    * Single activity architecture (with [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)) that defines navigation graphs
    * MVVM for presentation layer
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation))
    * [Android KTX](https://developer.android.com/kotlin/ktx) - Jetpack Kotlin extensions

## Presentation patterns layers
* View - Composable screens that consume state, apply effects and delegate events upstream.
* ViewModel - [AAC ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that manages and set the state of the corresponding screen. Additionally, it intercepts UI events as callbacks and produces side-effects. The ViewModel is scoped to the lifetime of the corresponding screen composable in the backstack.
* Model - Data source classes that retrieve content. In a Clean architecture context, one could use UseCases or Interactors that tap into repositories or data sources directly.

![](https://i.imgur.com/OdPje6D.png)

As the presentation layer is defined with MVVM, there are a two core components described:
* **State** - data class that holds the state content of the corresponding screen e.g. list of `FoodItem`, loading status etc. The state is exposed as a Compose runtime `MutableState` object from that perfectly matches the use-case of receiving continuous updates with initial value.

* **Effect** - plain object that signals one-time side-effect actions that should impact the UI e.g. triggering a navigation action, showing a Toast, SnackBar etc. Effects are exposed as `ChannelFlow` which behave as in each event is delivered to a single subscriber. An attempt to post an event without subscribers will suspend as soon as the channel buffer becomes full, waiting for a subscriber to appear.

Every screen/flow defines its own contract class that states all corresponding core components described above: state content and effects.

### Dependency injection
[Hilt](https://developer.android.com/training/dependency-injection/hilt-android) is used for Dependency Injection as a wrapper on top of [Dagger](https://github.com/google/dagger). 

Most of the dependencies are injected with `@Singleton` scope and are provided within the `FoodMenuApiProvider` module.

For ViewModels, we use the out-of-the-box `@HiltViewModel` annotation that injects them with the scope of the navigation graph composables that represent the screens.

### Decoupling Compose
Since Compose is a standalone declarative UI framework, one must try to decouple it from the Android framework as much as possible. In order to achieve this, the project uses an `EntryPointActivity` that defines a navigation graph where every screen is a composable.

The `EntryPointActivity` also collects `state` objects and passes them together with the `Effect` flows to each Screen composable. This way, the Activity is coupled with the navigation component and only screen (root level) composables. This causes the screen composables to only receive and interact with plain objects and Kotlin Flows, therefore trying to be platform agnostic as much as possible.
