# Migration Guide: Old API → Production API

## Overview

The iOS Screen Time implementation has been upgraded to production-grade architecture.
The old `IOSAppMonitor` and `IOSAppMonitorImpl` are **deprecated** and will be removed in a future version.

## What Changed?

### Old Approach (Deprecated)

```kotlin
// ❌ DEPRECATED - Don't use
val monitor = IOSAppMonitorImpl()
monitor.requestAuthorization()
monitor.shieldApp("com.example.app")
```

**Problems**:
- Global state with `IOSAppMonitorHelper`
- No dependency injection
- Hard to test
- Manual initialization required

### New Approach (Production)

```kotlin
// ✅ RECOMMENDED - Use this
class MyViewModel(
    private val screenTimeApi: ScreenTimeApi  // Injected by Koin
) : ViewModel() {
    suspend fun blockApp() {
        screenTimeApi.shieldApp("com.example.app")
            .onSuccess { /* Success */ }
            .onFailure { /* Error */ }
    }
}
```

**Benefits**:
- ✅ Dependency injection via Koin
- ✅ Fully testable
- ✅ Type-safe Result types
- ✅ Production-ready architecture

## Migration Steps

### Step 1: Update Imports

```kotlin
// Before
import id.compagnie.tawazn.platform.ios.IOSAppMonitor
import id.compagnie.tawazn.platform.ios.IOSAppMonitorImpl

// After
import id.compagnie.tawazn.platform.ios.ScreenTimeApi
```

### Step 2: Use Dependency Injection

```kotlin
// Before
class MyClass {
    private val monitor = IOSAppMonitorImpl()  // ❌
}

// After
class MyClass(
    private val screenTimeApi: ScreenTimeApi  // ✅ Injected
)
```

### Step 3: Update Function Calls

| Old API | New API |
|---------|---------|
| `monitor.requestAuthorization()` | `screenTimeApi.requestAuthorization()` |
| `monitor.isAuthorized()` | `screenTimeApi.isAuthorized()` |
| `monitor.shieldApp(id)` | `screenTimeApi.shieldApp(id)` |
| `monitor.unshieldApp(id)` | `screenTimeApi.unshieldApp(id)` |
| `monitor.setupActivityMonitoring()` | `screenTimeApi.setupActivityMonitoring()` |

### Step 4: Handle Results

```kotlin
// Before
monitor.shieldApp("com.app")  // Throws exception on error

// After
screenTimeApi.shieldApp("com.app")  // Returns Result<Unit>
    .onSuccess { /* Success */ }
    .onFailure { error -> /* Handle error */ }
```

## Complete Example

### Before (Deprecated)

```kotlin
class AppBlockingViewModel {
    private val monitor = IOSAppMonitorImpl()

    suspend fun initialize() {
        try {
            monitor.requestAuthorization()
            if (monitor.isAuthorized()) {
                monitor.shieldApp("com.example.distracting")
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
}
```

### After (Production)

```kotlin
class AppBlockingViewModel(
    private val screenTimeApi: ScreenTimeApi
) : ViewModel() {

    suspend fun initialize() {
        screenTimeApi.requestAuthorization()
            .onSuccess {
                if (screenTimeApi.isAuthorized()) {
                    screenTimeApi.shieldApp("com.example.distracting")
                        .onSuccess { /* Success */ }
                        .onFailure { error -> /* Handle error */ }
                }
            }
            .onFailure { error ->
                // Handle authorization error
            }
    }
}
```

## Deprecated Files

The following files are deprecated and will be removed:

- ❌ `IOSAppMonitor.kt` (interface)
- ❌ `IOSAppMonitorImpl.kt` (implementation)
- ❌ `IOSAppMonitorHelper` object
- ❌ `IOSScreenTimeHelper` interface
- ❌ `ScreenTimeHelperImpl.swift`
- ❌ `ScreenTimeHelperBridge` class

## New Files (Production)

Use these instead:

- ✅ `ScreenTimeApi.kt` (interface)
- ✅ `ScreenTimeApi.ios.kt` (implementation)
- ✅ `SwiftScreenTimeBridge.kt` (protocol)
- ✅ `SwiftScreenTimeBridgeImpl.swift` (Swift implementation)
- ✅ `IOSPlatformSync.kt` (orchestration service)
- ✅ `IOSPlatformModule.kt` (Koin DI module)

## Testing

### Before (Hard to Test)

```kotlin
// Can't inject mocks!
class MyClass {
    private val monitor = IOSAppMonitorImpl()
}
```

### After (Easy to Test)

```kotlin
@Test
fun `test blocking app`() = runTest {
    // Given
    val mockApi = MockScreenTimeApi()
    val viewModel = AppBlockingViewModel(mockApi)

    // When
    viewModel.blockApp("com.example.app")

    // Then
    assertTrue(mockApi.shieldAppCalled)
}
```

## Timeline

- **Now**: Old API marked as deprecated but still works
- **Version X.X**: Old API will show compiler warnings
- **Version Y.Y**: Old API will be removed

## Need Help?

- See `iosApp/PRODUCTION_ARCHITECTURE.md` for full architecture docs
- See `docs/KMP_IOS_BEST_PRACTICES.md` for patterns
- Check unit tests in `ScreenTimeApiTest.kt` for examples

## Summary

| Aspect | Old API | New API |
|--------|---------|---------|
| Architecture | Global state | Dependency injection |
| Testing | Hard | Easy with mocks |
| Error Handling | Exceptions | Result types |
| Type Safety | Runtime | Compile-time |
| Documentation | Basic | Comprehensive |
| Production Ready | ❌ No | ✅ Yes |

**Recommendation**: Migrate to the new API as soon as possible for better quality and maintainability.
