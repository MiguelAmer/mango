pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Mango Test"
include(":app")
include(":feature:productlist")
include(":feature:favorites")
include(":feature:profile")
include(":domain")
include(":data")
include(":core:network")
include(":core:database")
include(":core:designsystem")