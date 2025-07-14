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
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "ZubZub"
include(":app")
include(":feature:splash")
include(":core:util")
include(":core:navigation")
include(":core:ui")
include(":core:data")
include(":core:domain")
include(":feature:auth")
include(":feature:main")
include(":feature:home")
include(":feature:group-create")

include(":feature:group-enter")
include(":feature:group-cart")
