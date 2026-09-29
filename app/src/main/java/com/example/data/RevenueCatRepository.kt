package com.example.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Subscription states according to RevenueCat CustomerInfo entitlements.
 */
sealed class SubscriptionState {
    object Loading : SubscriptionState()
    object Free : SubscriptionState()
    object Pro : SubscriptionState()
    data class Error(val message: String) : SubscriptionState()
}

/**
 * Extension helper to provide localizedPriceString for RevenueCat Package.
 */
val Package.localizedPriceString: String
    get() = product.price.formatted

class RevenueCatRepository(
    private val context: Context
) {
    private val _subscriptionState = MutableStateFlow<SubscriptionState>(SubscriptionState.Loading)
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState.asStateFlow()

    private val _currentOfferings = MutableStateFlow<Offerings?>(null)
    val currentOfferings: StateFlow<Offerings?> = _currentOfferings.asStateFlow()

    companion object {
        const val ENTITLEMENT_PRO = "pro"
        private const val TAG = "RevenueCatRepo"
    }

    init {
        configureIfNeeded()
        if (Purchases.isConfigured) {
            fetchCustomerInfo()
            fetchOfferings()
        } else {
            _subscriptionState.value = SubscriptionState.Free
        }
    }

    fun isKeyConfigured(): Boolean {
        val apiKey = try {
            BuildConfig.REVENUECAT_API_KEY
        } catch (e: Throwable) {
            ""
        }
        return isRealApiKey(apiKey)
    }

    private fun isRealApiKey(key: String?): Boolean {
        if (key.isNullOrBlank()) return false
        val trimmed = key.trim()
        if (trimmed.contains("placeholder", ignoreCase = true)) return false
        if (trimmed.equals("MY_REVENUECAT_API_KEY", ignoreCase = true)) return false
        if (!trimmed.startsWith("test_") && !trimmed.startsWith("appl_") && !trimmed.startsWith("goog_")) return false
        return trimmed.length >= 10
    }

    fun configureIfNeeded() {
        if (!Purchases.isConfigured) {
            val apiKey = try {
                BuildConfig.REVENUECAT_API_KEY
            } catch (e: Throwable) {
                ""
            }

            if (!isRealApiKey(apiKey)) {
                Log.i(TAG, "REVENUECAT_API_KEY is not configured with a valid key. Add key in AI Studio Secrets panel.")
                _subscriptionState.value = SubscriptionState.Free
                return
            }

            try {
                Purchases.logLevel = LogLevel.WARN
                Purchases.configure(
                    PurchasesConfiguration.Builder(context.applicationContext, apiKey.trim())
                        .build()
                )
                Log.d(TAG, "RevenueCat configured with key: ${apiKey.take(8)}...")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to configure RevenueCat: ${e.message}")
                _subscriptionState.value = SubscriptionState.Free
            }
        }
    }

    fun fetchCustomerInfo() {
        if (!Purchases.isConfigured) {
            _subscriptionState.value = SubscriptionState.Free
            return
        }

        Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                updateEntitlementFromCustomerInfo(customerInfo)
            }

            override fun onError(error: PurchasesError) {
                Log.w(TAG, "Error fetching customer info: ${error.message}")
                if (_subscriptionState.value is SubscriptionState.Loading) {
                    _subscriptionState.value = SubscriptionState.Free
                }
            }
        })
    }

    fun fetchOfferings() {
        if (!Purchases.isConfigured) return

        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: Offerings) {
                _currentOfferings.value = offerings
                Log.d(TAG, "Fetched offerings: current=${offerings.current?.identifier}, packages=${offerings.current?.availablePackages?.size}")
            }

            override fun onError(error: PurchasesError) {
                Log.w(TAG, "Error fetching offerings: ${error.message}")
            }
        })
    }

    private fun updateEntitlementFromCustomerInfo(customerInfo: CustomerInfo) {
        val isProActive = customerInfo.entitlements.active[ENTITLEMENT_PRO] != null
        Log.d(TAG, "CustomerInfo.entitlements.active['$ENTITLEMENT_PRO'] != null: $isProActive")
        _subscriptionState.value = if (isProActive) {
            SubscriptionState.Pro
        } else {
            SubscriptionState.Free
        }
    }

    fun purchasePackage(
        activity: Activity,
        rcPackage: Package,
        onSuccess: () -> Unit,
        onCancelled: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!Purchases.isConfigured) {
            onError("RevenueCat is not configured. Please add REVENUECAT_API_KEY in the AI Studio Secrets panel.")
            return
        }

        val params = PurchaseParams.Builder(activity, rcPackage).build()
        Purchases.sharedInstance.purchase(params, object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                updateEntitlementFromCustomerInfo(customerInfo)
                val proActive = customerInfo.entitlements.active[ENTITLEMENT_PRO] != null
                if (proActive) {
                    onSuccess()
                } else {
                    onError("Purchase completed but 'pro' entitlement was not granted.")
                }
            }

            override fun onError(error: PurchasesError, userCancelled: Boolean) {
                if (userCancelled) {
                    Log.d(TAG, "Purchase cancelled by user in Test Store")
                    onCancelled()
                } else {
                    Log.w(TAG, "Purchase failed: ${error.message}")
                    onError(error.message)
                }
            }
        })
    }

    fun restorePurchases(
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!Purchases.isConfigured) {
            onError("RevenueCat is not configured. Please add REVENUECAT_API_KEY in the AI Studio Secrets panel.")
            return
        }

        Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                updateEntitlementFromCustomerInfo(customerInfo)
                val isPro = customerInfo.entitlements.active[ENTITLEMENT_PRO] != null
                onSuccess(isPro)
            }

            override fun onError(error: PurchasesError) {
                Log.w(TAG, "Restore purchases failed: ${error.message}")
                onError(error.message)
            }
        })
    }
}

typealias PurchasesRepository = RevenueCatRepository
