import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
    `maven-publish`
    signing
}

android {
    namespace = "com.example.naventryscope"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.mercari"
                artifactId = "nav-entry-scope"
                version = System.getenv("VERSION") ?: libs.versions.navEntryScope.get()

                pom {
                    name.set("NavEntryScope Library")
                    description.set("Navigation entry scoping library for Jetpack Compose - manages ViewModel lifecycle tied to navigation back stack entries")
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
            sign(publishing.publications["release"])
        }
    }
}