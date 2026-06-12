package com.test.mandiri.news.common.extension

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun FragmentActivity.getLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch(context = context) { block.invoke(this) }

fun <T> FragmentActivity.collectFlow(flow: Flow<T>, action: (T) -> Unit) {
    getLaunch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { action.invoke(it) }
        }
    }
}
