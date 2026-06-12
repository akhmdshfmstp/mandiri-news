# Mandiri News

Android news app built with Clean Architecture and multi-module setup, consuming [NewsAPI](https://newsapi.org/).

## Features

- Browse news by category
- Browse news sources per category with endless scroll
- Search sources within a category
- Browse articles per source with endless scroll and pull-to-refresh
- Search articles
- Read full article via WebView
- API key stored in C++ native code (not in `local.properties` or `BuildConfig`)

## Tech Stack

| Layer | Tech |
|---|---|
| Language | Kotlin 1.9.23 |
| Architecture | Clean Architecture + MVVM + UDF |
| DI | Hilt |
| Async | Coroutines + StateFlow |
| Network | Retrofit + OkHttp + Gson |
| Image | Coil |
| HTTP inspector | Chucker (debug only) |
| Testing | JUnit + MockK + Turbine |

## Module Structure

```
app/                    → Application entry point, SplashActivity
common/                 → Pure Kotlin: Resource, ErrorResponse
common-android/         → BaseActivity, BaseViewModel, extensions
core/
  network/              → OkHttpClient, Retrofit, NativeHelper (JNI)
  router/               → In-app navigation via path + query params
domain/
  news/                 → Models, NewsRepository interface, use cases
data/
  news/                 → NewsApiService, NewsRepositoryImpl, mappers
feature/
  news/                 → Categories, Sources, Articles, ArticleDetail
```

