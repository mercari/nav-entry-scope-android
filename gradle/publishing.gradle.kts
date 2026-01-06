// Shared publishing configuration for Vanniktech Maven Publish plugin
// Secrets are loaded from ~/.gradle/gradle.properties (see README for required keys)

// Get version from VERSION property or fall back to navEntryScopeVersion
val publishVersion = providers.gradleProperty("VERSION").orNull
    ?: project.property("navEntryScopeVersion") as String
project.extra.set("publishVersion", publishVersion)

// Shared POM configuration function - available via extra properties
project.extra.set("configurePom") { pom: org.gradle.api.publish.maven.MavenPom ->
    pom.apply {
        url.set("https://github.com/mercari/nav-entry-scope-android")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/mercari/nav-entry-scope-android/blob/main/LICENSE.txt")
            }
        }
        developers {
            developer {
                id.set("bxttx")
                name.set("Luca Bettelli")
                email.set("l-bettelli@mercari.com")
            }
            developer {
                id.set("rsfez")
                name.set("Robin Sfez")
                email.set("rsfez@mercari.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/mercari/nav-entry-scope-android.git")
            developerConnection.set("scm:git:ssh://github.com/mercari/nav-entry-scope-android.git")
            url.set("https://github.com/mercari/nav-entry-scope-android")
        }
    }
}
