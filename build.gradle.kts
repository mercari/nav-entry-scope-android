// Top-level build file where you can add configuration options common to all sub-projects/modules.

val signingProperties = java.util.Properties().apply {
    file("signing.local").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

fun getSigningProperty(key: String): String? =
    providers.environmentVariable(key).orNull ?: signingProperties.getProperty(key)

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    packageGroup.set("com.mercari")
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(getSigningProperty("SONATYPE_USERNAME"))
            password.set(getSigningProperty("SONATYPE_PASSWORD"))
        }
    }
}

allprojects {
    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "snapshot"
                    url = uri("https://central.sonatype.com/repository/maven-snapshots/")
                    credentials {
                        username = getSigningProperty("SONATYPE_USERNAME") ?: ""
                        password = getSigningProperty("SONATYPE_PASSWORD") ?: ""
                    }
                }
            }
        }
    }
}