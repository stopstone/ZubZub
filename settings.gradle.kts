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

rootProject.name = "ZubZub"
include(":app")
include(":feature:splash")
include(":core:util")
include(":core:navigation")
include(":core:ui")
include(":feature:auth")
include(":feature:main")
include(":feature:home")
