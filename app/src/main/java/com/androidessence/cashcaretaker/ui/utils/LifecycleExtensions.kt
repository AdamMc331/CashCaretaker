package com.androidessence.cashcaretaker.ui.utils

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * This is a wrapper around [LifecycleCoroutineScope.launchWhenResumed] to call [Flow.collect]
 * inside our [lifecycleCoroutineScope].
 */
fun <T> Flow<T>.launchWhenResumed(
    lifecycleCoroutineScope: LifecycleCoroutineScope,
) {
    lifecycleCoroutineScope.launchWhenResumed {
        collect()
    }
}
