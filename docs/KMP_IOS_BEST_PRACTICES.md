# KMP iOS Best Practices - Swift to Kotlin Bridging

## Current Implementation Analysis

### What We Built (Quick Fix Approach)

```kotlin
// ❌ Global mutable singleton
object IOSAppMonitorHelper {
    var instance: IOSScreenTimeHelper? = null
    fun initialize(helper: IOSScreenTimeHelper) {
        instance = helper
    }
}
```

```swift
// ❌ Another global singleton
class ScreenTimeHelperBridge {
    static let shared = ScreenTimeHelperBridge()
    var helper: ScreenTimeHelperImpl?
}
```

### Problems with Current Approach

1. **Global Mutable State** - Multiple singletons make testing difficult
2. **No Compile-Time Safety** - Runtime initialization can fail silently
3. **Manual Wiring** - Easy to forget initialization
4. **Not Idiomatic KMP** - Doesn't follow Kotlin Multiplatform patterns
5. **Hard to Test** - Can't inject mocks easily

## Best Practices for KMP iOS

### ✅ Option 1: Expect/Actual with Factory (Recommended for Your Case)

**Best for:** Platform-specific APIs that can't be accessed via cinterop

```kotlin
// commonMain/kotlin/platform/ScreenTimeApi.kt
expect interface ScreenTimeApi {
    suspend fun requestAuthorization(): Result<Unit>
    fun isAuthorized(): Boolean
    suspend fun shieldApp(bundleId: String): Result<Unit>
}

expect fun createScreenTimeApi(): ScreenTimeApi
```

```kotlin
// iosMain/kotlin/platform/ScreenTimeApi.ios.kt
actual interface ScreenTimeApi {
    actual suspend fun requestAuthorization(): Result<Unit>
    actual fun isAuthorized(): Boolean
    actual suspend fun shieldApp(bundleId: String): Result<Unit>
}

private var swiftBridgeInstance: SwiftScreenTimeBridge? = null

actual fun createScreenTimeApi(): ScreenTimeApi {
    return IOSScreenTimeApiImpl(
        swiftBridgeInstance ?: throw IllegalStateException(
            "Swift bridge not initialized. Call initializeSwiftBridge() from Swift."
        )
    )
}

// Called from Swift during app initialization
fun initializeSwiftBridge(bridge: SwiftScreenTimeBridge) {
    swiftBridgeInstance = bridge
}

interface SwiftScreenTimeBridge {
    fun requestAuth(completion: (Boolean, String?) -> Unit)
    fun isAuthorized(): Boolean
    fun shieldApp(bundleId: String): Boolean
}

private class IOSScreenTimeApiImpl(
    private val bridge: SwiftScreenTimeBridge
) : ScreenTimeApi {
    override suspend fun requestAuthorization(): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            bridge.requestAuth { success, error ->
                if (success) cont.resume(Result.success(Unit))
                else cont.resume(Result.failure(Exception(error)))
            }
        }

    override fun isAuthorized() = bridge.isAuthorized()

    override suspend fun shieldApp(bundleId: String): Result<Unit> = runCatching {
        if (!bridge.shieldApp(bundleId)) throw Exception("Failed to shield")
    }
}
```

```swift
// Swift: Initialize in App
import ComposeApp

@available(iOS 15.0, *)
class SwiftScreenTimeBridgeImpl: SwiftScreenTimeBridge {
    private let manager = ScreenTimeBridge.shared

    func requestAuth(completion: @escaping (Bool, String?) -> Void) {
        manager.requestAuthorization(completion: completion)
    }

    func isAuthorized() -> Bool {
        return manager.isAuthorized()
    }

    func shieldApp(bundleId: String) -> Bool {
        return manager.shieldApp(bundleIdentifier: bundleId)
    }
}

@main
struct iOSApp: App {
    init() {
        if #available(iOS 15.0, *) {
            let bridge = SwiftScreenTimeBridgeImpl()
            ScreenTimeApiKt.initializeSwiftBridge(bridge: bridge)
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

**Advantages:**
- ✅ Single initialization point
- ✅ Type-safe Swift protocol
- ✅ Clear error messages if not initialized
- ✅ Works with platform-specific code that can't be in framework

---

### ✅ Option 2: Koin Dependency Injection (Best for Large Projects)

**Best for:** Production apps, large codebases, better testability

```kotlin
// commonMain/kotlin/di/CommonModule.kt
val commonModule = module {
    single<ScreenTimeApi> { getScreenTimeApi() }
}

// iosMain/kotlin/di/IOSModule.kt
val iosModule = module {
    single<ScreenTimeApi> { IOSScreenTimeApiImpl(get()) }
    single<SwiftBridge> { getSwiftBridge() }
}

// iosMain/kotlin/platform/IOSApp.kt
fun initializeKoinIOS(swiftBridge: SwiftBridge) {
    storeSwiftBridge(swiftBridge)

    startKoin {
        modules(commonModule, iosModule)
    }
}
```

```swift
@main
struct iOSApp: App {
    init() {
        if #available(iOS 15.0, *) {
            let bridge = SwiftScreenTimeBridgeImpl()
            IOSAppKt.initializeKoinIOS(swiftBridge: bridge)
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

**Advantages:**
- ✅ Professional dependency management
- ✅ Easy to test with mocks
- ✅ Scoped dependencies
- ✅ Works well with ViewModels

---

### ✅ Option 3: Direct Kotlin/Native CInterop (Most Native)

**Best for:** Accessing iOS frameworks directly, no Swift wrapper needed

```kotlin
// iosMain/kotlin/platform/ScreenTimeApi.ios.kt
import platform.FamilyControls.*
import platform.ManagedSettings.*
import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
actual class ScreenTimeApiImpl : ScreenTimeApi {
    private val authCenter = AuthorizationCenter.shared
    private val settingsStore = ManagedSettingsStore()

    override suspend fun requestAuthorization(): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            authCenter.requestAuthorizationFor(
                FamilyActivitySelectionIndividual
            ) { error ->
                if (error == null) cont.resume(Result.success(Unit))
                else cont.resume(Result.failure(Exception(error.localizedDescription)))
            }
        }

    override fun isAuthorized(): Boolean {
        return authCenter.authorizationStatus == AuthorizationStatusApproved
    }

    override suspend fun shieldApp(bundleId: String): Result<Unit> = runCatching {
        val token = ApplicationToken(bundleId)
        settingsStore.shield.applications = setOf(token)
    }
}
```

**Advantages:**
- ✅ No Swift wrapper needed
- ✅ Direct access to iOS APIs
- ✅ Type-safe at compile time
- ✅ Most "native" KMP approach

**Disadvantages:**
- ❌ Requires proper cinterop configuration
- ❌ Complex for custom Swift classes
- ❌ Limited to Objective-C compatible APIs

---

## Comparison Table

| Approach | Complexity | Type Safety | Testability | Best For |
|----------|-----------|-------------|-------------|----------|
| **Current (Globals)** | Low | ⚠️ Runtime | ❌ Poor | Quick prototypes |
| **Expect/Actual + Factory** | Medium | ✅ Compile | ✅ Good | Platform APIs |
| **Koin DI** | Medium-High | ✅ Compile | ✅ Excellent | Production apps |
| **Direct CInterop** | High | ✅ Compile | ✅ Good | Framework APIs |

---

## Recommendation for Your Project

For the **Tawazn** screen time app, I recommend:

### Phase 1: Current State (What We Built)
- ✅ **Keep it for now** - Gets the app running
- ✅ **Works for MVP** - Functional and understandable
- ⚠️ **Not production-ready** - But fine for development

### Phase 2: Refactor to Expect/Actual (Next Step)
```kotlin
// This is what you should migrate to
expect interface ScreenTimeApi {
    suspend fun requestAuthorization(): Result<Unit>
    fun isAuthorized(): Boolean
    suspend fun shieldApp(bundleId: String): Result<Unit>
}

actual fun createScreenTimeApi(): ScreenTimeApi = ...
```

**Why:**
- Removes global state
- Better type safety
- Still simple to understand
- Easy migration path

### Phase 3: Add Koin DI (Production)
```kotlin
val appModule = module {
    single<ScreenTimeApi> { createScreenTimeApi() }
    viewModel { AppBlockingViewModel(get()) }
}
```

**Why:**
- Professional architecture
- Easy testing
- Scales with app complexity

---

## Migration Guide: Current → Best Practice

### Step 1: Create Interface in Common

```kotlin
// platform/src/commonMain/kotlin/ScreenTimeApi.kt
expect interface ScreenTimeApi {
    suspend fun requestAuthorization(): Result<Unit>
    fun isAuthorized(): Boolean
    suspend fun shieldApp(bundleId: String): Result<Unit>
    suspend fun unshieldApp(bundleId: String): Result<Unit>
}

expect fun createScreenTimeApi(): ScreenTimeApi
```

### Step 2: Implement in iOS Main

```kotlin
// platform/src/iosMain/kotlin/ScreenTimeApi.ios.kt
actual interface ScreenTimeApi { /* actual implementation */ }

private var swiftBridge: SwiftScreenTimeBridge? = null

fun initializeScreenTimeBridge(bridge: SwiftScreenTimeBridge) {
    swiftBridge = bridge
}

actual fun createScreenTimeApi(): ScreenTimeApi {
    return IOSScreenTimeApiImpl(
        swiftBridge ?: error("Bridge not initialized")
    )
}
```

### Step 3: Update Swift Initialization

```swift
@main
struct iOSApp: App {
    init() {
        let bridge = SwiftScreenTimeBridgeImpl()
        ScreenTimeApiKt.initializeScreenTimeBridge(bridge: bridge)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

### Step 4: Remove Globals

Delete:
- ❌ `IOSAppMonitorHelper` object
- ❌ `ScreenTimeHelperBridge` class
- ❌ `IOSScreenTimeHelper` interface (replace with proper expect/actual)

---

## Testing Benefits

### Current Approach (Hard to Test)
```kotlin
class AppBlockingViewModel {
    private val monitor = IOSAppMonitorImpl() // ❌ Can't mock

    suspend fun blockApp(id: String) {
        monitor.shieldApp(id) // ❌ Hard to test
    }
}
```

### Best Practice (Easy to Test)
```kotlin
class AppBlockingViewModel(
    private val screenTimeApi: ScreenTimeApi // ✅ Interface injection
) {
    suspend fun blockApp(id: String) {
        screenTimeApi.shieldApp(id) // ✅ Easy to mock
    }
}

// In tests:
class MockScreenTimeApi : ScreenTimeApi {
    override suspend fun shieldApp(id: String) = Result.success(Unit)
}

@Test
fun testBlockApp() {
    val viewModel = AppBlockingViewModel(MockScreenTimeApi())
    // Test easily!
}
```

---

## Summary

**Current Implementation:**
- ✅ Works and gets you running
- ⚠️ Not idiomatic KMP
- ❌ Hard to test
- 💡 OK for MVP/prototype

**Best Practice:**
- ✅ Expect/Actual pattern
- ✅ Dependency injection
- ✅ Type-safe interfaces
- ✅ Easy to test
- 💡 Required for production

**My Advice:**
1. **Keep current implementation** for now to get the app working
2. **Plan migration** to expect/actual pattern
3. **Add Koin DI** when the app grows
4. **Refactor incrementally** - not all at once

The most important thing is that your app works! The refactoring can happen as you grow. 🚀
