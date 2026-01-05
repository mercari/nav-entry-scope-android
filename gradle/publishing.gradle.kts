import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

val signingProperties = java.util.Properties().apply {
    rootProject.file("signing.local").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

fun getSigningProperty(key: String): String? =
    System.getenv(key) ?: signingProperties.getProperty(key)

fun getSigningPropertyWithNewlines(key: String): String? =
    System.getenv(key) ?: signingProperties.getProperty(key)?.replace("\\n", "\n")

extra["signingProperties"] = signingProperties
extra["getSigningProperty"] = ::getSigningProperty

plugins.withId("maven-publish") {
    afterEvaluate {
        extensions.configure<PublishingExtension> {
            publications.withType<MavenPublication>().configureEach {
                groupId = "com.mercari"
                version = getSigningProperty("VERSION") ?: project.property("navEntryScopeVersion") as String

                pom {
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
        }
    }

    plugins.withId("signing") {
        val publishing = extensions.getByType<PublishingExtension>()
        val signing = extensions.getByType<SigningExtension>()

        val signingKey = getSigningPropertyWithNewlines("SIGNING_KEY")
        val signingPassword = getSigningProperty("SIGNING_PASSWORD")

        if (signingKey != null && signingPassword != null) {
            signing.useInMemoryPgpKeys(signingKey, signingPassword)
            signing.sign(publishing.publications)
        }
    }
}
