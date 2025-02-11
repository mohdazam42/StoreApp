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

rootProject.name = "StoreApp"
include(":app")
include(":network")
include(":common")
include(":feature:productlist:data")
include(":feature:productlist:domain")
include(":feature:productlist:presentation")
include(":feature:productdetail:data")
include(":feature:productdetail:domain")
include(":feature:productdetail:presentation")
