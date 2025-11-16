package id.compagnie.tawazn.platform.ios

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for ScreenTimeApi implementation
 *
 * These tests demonstrate the testability of the production-grade architecture.
 * They use a mock implementation of SwiftScreenTimeBridge to test the Kotlin logic
 * without requiring actual iOS Screen Time APIs.
 */
class ScreenTimeApiTest {

    @Test
    fun `IOSScreenTimeApi returns authorized when bridge reports authorized`() {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(isAuthorized = true)
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.isAuthorized()

        // Then
        assertTrue(result, "Should be authorized when bridge reports authorized")
    }

    @Test
    fun `IOSScreenTimeApi returns not authorized when bridge reports not authorized`() {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(isAuthorized = false)
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.isAuthorized()

        // Then
        assertFalse(result, "Should not be authorized when bridge reports not authorized")
    }

    @Test
    fun `IOSScreenTimeApi correctly maps authorization status`() {
        val testCases = listOf(
            "not_determined" to AuthorizationStatus.NOT_DETERMINED,
            "denied" to AuthorizationStatus.DENIED,
            "approved" to AuthorizationStatus.APPROVED,
            "unknown" to AuthorizationStatus.UNKNOWN
        )

        testCases.forEach { (swiftStatus, expectedStatus) ->
            // Given
            val mockBridge = MockSwiftScreenTimeBridge(authorizationStatus = swiftStatus)
            val screenTimeApi = IOSScreenTimeApi(mockBridge)

            // When
            val result = screenTimeApi.getAuthorizationStatus()

            // Then
            assertEquals(
                expectedStatus,
                result,
                "Status '$swiftStatus' should map to $expectedStatus"
            )
        }
    }

    @Test
    fun `requestAuthorization succeeds when bridge succeeds`() = runTest {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(authorizationSucceeds = true)
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.requestAuthorization()

        // Then
        assertTrue(result.isSuccess, "Authorization should succeed when bridge succeeds")
        assertTrue(mockBridge.requestAuthorizationCalled, "Should call bridge authorization")
    }

    @Test
    fun `requestAuthorization fails when bridge fails`() = runTest {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(
            authorizationSucceeds = false,
            authorizationError = "User denied"
        )
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.requestAuthorization()

        // Then
        assertTrue(result.isFailure, "Authorization should fail when bridge fails")
        assertTrue(mockBridge.requestAuthorizationCalled, "Should call bridge authorization")
    }

    @Test
    fun `shieldApp succeeds when authorized and bridge succeeds`() = runTest {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(
            isAuthorized = true,
            shieldAppSucceeds = true
        )
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.shieldApp("com.example.app")

        // Then
        assertTrue(result.isSuccess, "Shield should succeed when authorized")
        assertTrue(mockBridge.shieldAppCalled, "Should call bridge shieldApp")
        assertEquals("com.example.app", mockBridge.lastShieldedAppId)
    }

    @Test
    fun `shieldApp fails when not authorized`() = runTest {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(isAuthorized = false)
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.shieldApp("com.example.app")

        // Then
        assertTrue(result.isFailure, "Shield should fail when not authorized")
        assertFalse(mockBridge.shieldAppCalled, "Should not call bridge when not authorized")
    }

    @Test
    fun `getShieldedApps returns list from bridge`() = runTest {
        // Given
        val shieldedApps = listOf("com.app1", "com.app2", "com.app3")
        val mockBridge = MockSwiftScreenTimeBridge(
            isAuthorized = true,
            shieldedApps = shieldedApps
        )
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.getShieldedApps()

        // Then
        assertTrue(result.isSuccess, "Should succeed")
        assertEquals(shieldedApps, result.getOrNull(), "Should return shielded apps from bridge")
    }

    @Test
    fun `isAvailable returns bridge availability`() {
        // Given
        val mockBridge = MockSwiftScreenTimeBridge(isAvailable = true)
        val screenTimeApi = IOSScreenTimeApi(mockBridge)

        // When
        val result = screenTimeApi.isAvailable()

        // Then
        assertTrue(result, "Should return bridge availability")
    }
}

/**
 * Mock implementation of SwiftScreenTimeBridge for testing
 *
 * This demonstrates how the dependency injection architecture
 * makes the code highly testable without requiring actual iOS APIs.
 */
private class MockSwiftScreenTimeBridge(
    private val isAuthorized: Boolean = false,
    private val authorizationStatus: String = "not_determined",
    private val authorizationSucceeds: Boolean = true,
    private val authorizationError: String? = null,
    private val shieldAppSucceeds: Boolean = true,
    private val shieldedApps: List<String> = emptyList(),
    private val isAvailable: Boolean = true
) : SwiftScreenTimeBridge {

    var requestAuthorizationCalled = false
    var shieldAppCalled = false
    var lastShieldedAppId: String? = null

    override fun requestAuthorization(completion: (success: Boolean, errorMessage: String?) -> Void) {
        requestAuthorizationCalled = true
        completion(authorizationSucceeds, authorizationError)
    }

    override fun isAuthorized(): Boolean = isAuthorized

    override fun getAuthorizationStatus(): String = authorizationStatus

    override fun shieldApp(bundleIdentifier: String): Boolean {
        shieldAppCalled = true
        lastShieldedAppId = bundleIdentifier
        return shieldAppSucceeds
    }

    override fun shieldApps(bundleIdentifiers: List<String>): Boolean = shieldAppSucceeds

    override fun unshieldApp(bundleIdentifier: String): Boolean = true

    override fun unshieldAllApps(): Boolean = true

    override fun getShieldedApps(): List<String> = shieldedApps

    override fun startMonitoring(activityName: String): Boolean = true

    override fun stopMonitoring(activityName: String): Boolean = true

    override fun isAvailable(): Boolean = isAvailable
}
