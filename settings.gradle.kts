pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Photos"

include(":app")
include(":benchmark")
include(":config")
include(":domain")
include(":api")
include(":data")
include(":common")
include(":navigation")
include(":common-ui")
include(":feature:recommendations")
include(":feature:feedback")
include(":feature:curated-photos")
include(":feature:wallpaper")
include(":feature:photo-details")
include(":feature:search")
include(":feature:favorites")
include(":feature:preferences")
include(":theme-manager")
include(":account")
include(":compose-ui")
include(":photo-usecase")
