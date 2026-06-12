package com.test.mandiri.news.di

import com.test.mandiri.news.core.router.api.Router
import com.test.mandiri.news.core.router.impl.RouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRouter(): Router {
        return RouterImpl()
    }

    @Singleton
    @Provides
    @AppScope
    fun provideAppCoroutineScope(): CoroutineScope {
        return CoroutineScope(context = SupervisorJob() + Dispatchers.Main.immediate)
    }

}
