package com.test.mandiri.news

import android.app.Application
import com.test.mandiri.news.core.router.api.RouteHandler
import com.test.mandiri.news.core.router.api.Router
import com.test.mandiri.news.di.AppScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class MandiriNewsApp : Application() {

    @Inject
    @AppScope
    lateinit var appCoroutineScope: CoroutineScope

    @Inject
    lateinit var router: Provider<Router>

    @Inject
    lateinit var routeHandlers: Provider<Set<RouteHandler>>

    override fun onCreate() {
        super.onCreate()
        initializeRouter()
    }

    private fun initializeRouter() {
        appCoroutineScope.launch(context = Dispatchers.Default) {
            router.get().register(routeHandlers = routeHandlers.get().toList())
        }
    }
}
