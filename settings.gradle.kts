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

rootProject.name = "NavEntryScope Sample"
include(":app")

include(":nav-entry-scope-lib")
project(":nav-entry-scope-lib").projectDir = File(rootDir, "nav-entry-scope/lib")

include(":nav-entry-scope-processor")
project(":nav-entry-scope-processor").projectDir = File(rootDir, "nav-entry-scope/processor")

