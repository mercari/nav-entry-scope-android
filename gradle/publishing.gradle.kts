// Shared publishing configuration for Vanniktech Maven Publish plugin
// Secrets are loaded from ~/.gradle/gradle.properties (see README for required keys)

// Get version from PUBLISH_VERSION property or fall back to navEntryScopeVersion
val publishVersion = providers.gradleProperty("PUBLISH_VERSION").orNull
    ?: project.property("navEntryScopeVersion") as String
project.extra.set("publishVersion", publishVersion)

// Check if this is a SNAPSHOT version
val isSnapshot = publishVersion.endsWith("-SNAPSHOT")
project.extra.set("isSnapshot", isSnapshot)

// Support both in-memory signing (CI) and file-based signing (local)
val hasSigningCredentials = project.findProperty("signingInMemoryKey") != null
    || project.findProperty("signing.secretKeyRingFile") != null
project.extra.set("hasSigningCredentials", hasSigningCredentials)


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
