import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
    `maven-publish`
    jacoco
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
        debug {
            enableUnitTestCoverage = true
        }
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
                version = "1.0.0"

                pom {
                    name.set("NavEntryScope Library")
                    description.set("Core library for NavEntryScope pattern")
                }
            }
        }
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(true)
    }

    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java"))
    classDirectories.setFrom(files("${project.buildDir}/tmp/kotlin-classes/debug"))
    executionData.setFrom(files("${project.buildDir}/jacoco/testDebugUnitTest.exec"))
}