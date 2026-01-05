import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    signing
}

apply(from = rootProject.file("gradle/unit-test-dependencies.gradle.kts"))

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlinpoet.ksp)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mercari"
            artifactId = "nav-entry-scope-processor"
            version = System.getenv("VERSION") ?: libs.versions.navEntryScope.get()

            from(components["java"])

            pom {
                name.set("NavEntryScope Processor")
                description.set("KSP annotation processor for NavEntryScope - generates scoped ViewModel factories for Jetpack Compose navigation")
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

signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["maven"])
    }
}