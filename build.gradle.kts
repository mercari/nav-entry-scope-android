// Top-level build file where you can add configuration options common to all sub-projects/modules.
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
            username.set(providers.environmentVariable("SONATYPE_USERNAME"))
            password.set(providers.environmentVariable("SONATYPE_PASSWORD"))
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
                        username = providers.environmentVariable("SONATYPE_USERNAME").orNull ?: ""
                        password = providers.environmentVariable("SONATYPE_PASSWORD").orNull ?: ""
                    }
                }
            }
        }
    }
}