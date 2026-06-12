package com.test.mandiri.news.core.router.impl

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.test.mandiri.news.core.router.api.Origin
import com.test.mandiri.news.core.router.api.Router
import com.test.mandiri.news.core.router.api.Transmitter
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

const val INTERNAL_SCHEME = "internal"
const val INTERNAL_HOST = "route"

/** Convenient extension for destination screen navigation. */
fun Router.navigate(
    context: Context,
    path: String,
    query: Map<String, Any> = mapOf(),
    navigator: (Intent) -> Unit,
    onError: (cause: Throwable) -> Unit = {},
) = route(
    context = context,
    path = path,
    query = query,
    dispatch = navigator,
    onError = onError,
)

/** Convenient extension for destination fragment binding. */
fun Router.bind(
    context: Context,
    path: String,
    query: Map<String, Any> = mapOf(),
    binder: (Fragment) -> Unit,
    onError: (cause: Throwable) -> Unit = {},
) = route(
    context = context,
    path = path,
    query = query,
    dispatch = binder,
    onError = onError,
)

/** Convenient extension to load any destination object result. */
fun Router.connect(
    context: Context,
    path: String,
    query: Map<String, Any> = mapOf(),
    onResult: (Any) -> Unit,
    onError: (cause: Throwable) -> Unit = {},
) = route(
    context = context,
    path = path,
    query = query,
    dispatch = onResult,
    onError = onError,
)

/* example for url : https://mobile.example/section/detail?id=123 --> it must have path and query */
fun Router.navigateUrl(
    context: Context,
    url: String,
    navigator: (Intent) -> Unit,
    onError: (cause: Throwable) -> Unit = {},
) {
    val query: MutableMap<String, Any> = mutableMapOf()
    val uri = url.toHttpUrlOrNull()
    val path = uri?.encodedPath.orEmpty()
    val size = uri?.querySize ?: 0
    for (i in 0 until size) {
        query[uri?.queryParameterName(i).orEmpty()] = uri?.queryParameterValue(i).orEmpty()
    }
    route(
        context = context,
        path = path,
        query = query,
        dispatch = navigator,
        onError = onError,
    )
}

private inline fun <reified T> Router.route(
    context: Context,
    path: String,
    query: Map<String, Any> = mapOf(),
    crossinline dispatch: (T) -> Unit,
    crossinline onError: (cause: Throwable) -> Unit,
) {
    val normalizedPath = path.trim().run {
        if (isEmpty()) return
        if (first() == '/') drop(1)
        else this
    }

    val queryBuilder = StringBuilder()
    for ((key, value) in query) {
        if (queryBuilder.isEmpty()) queryBuilder.append('?')
        else queryBuilder.append('&')

        queryBuilder.append("$key=$value")
    }

    val uri = "$INTERNAL_SCHEME://$INTERNAL_HOST/$normalizedPath$queryBuilder"
    val transmitter = Transmitter { component ->
        if (component !is T) return@Transmitter
        dispatch(component)
    }
    val origin = Origin(
        context = context,
        uri = uri,
        transmitter = transmitter,
    )

    route(
        origin = origin,
        onError = { onError(it) },
    )
}
