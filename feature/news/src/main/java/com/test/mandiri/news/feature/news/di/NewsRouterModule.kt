package com.test.mandiri.news.feature.news.di

import com.test.mandiri.news.core.router.api.RouteHandler
import com.test.mandiri.news.feature.news.articledetail.ArticleDetailRouteHandler
import com.test.mandiri.news.feature.news.articles.ArticlesRouteHandler
import com.test.mandiri.news.feature.news.categories.CategoriesRouteHandler
import com.test.mandiri.news.feature.news.sources.SourcesRouteHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface NewsRouterModule {

    @Binds
    @IntoSet
    fun bindCategoriesRouteHandler(handler: CategoriesRouteHandler): RouteHandler

    @Binds
    @IntoSet
    fun bindSourcesRouteHandler(handler: SourcesRouteHandler): RouteHandler

    @Binds
    @IntoSet
    fun bindArticlesRouteHandler(handler: ArticlesRouteHandler): RouteHandler

    @Binds
    @IntoSet
    fun bindArticleDetailRouteHandler(handler: ArticleDetailRouteHandler): RouteHandler
}
