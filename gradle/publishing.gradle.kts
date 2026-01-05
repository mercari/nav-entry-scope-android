import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

plugins.withId("maven-publish") {
    afterEvaluate {
        extensions.configure<PublishingExtension> {
            publications.withType<MavenPublication>().configureEach {
                groupId = "com.mercari"
                version = System.getenv("VERSION") ?: project.property("navEntryScopeVersion") as String

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
}
