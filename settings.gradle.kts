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
    }
}

rootProject.name = "AISystem"
include(":app")
include(":core:network")
include(":core:storage")
include(":core:common")
include(":feature:chat")
include(":feature:voice")
include(":feature:settings")
include(":feature:debug")
