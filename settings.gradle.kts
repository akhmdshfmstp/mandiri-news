pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "MandiriNews"
include(":app")

include(":common")
include(":common-android")

include(":core")
include(":core:router")
include(":core:network")

include(":domain")
include(":domain:news")

include(":feature")
include(":feature:news")

include(":data")
include(":data:news")
