import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("kotlin-kapt")
    jacoco
}

apply(from = rootProject.file("gradle/unit-test-dependencies.gradle.kts"))

android {
    namespace = "com.mercari.naventryscopesample"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.naventryscopesample"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    hilt {
        enableAggregatingTask = false
    }

    @Suppress("UnstableApiUsage")
    testFixtures {
        enable = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.navEntryScopeLib)
    ksp(libs.navEntryScopeProcessor)

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testFixturesImplementation(libs.kotlinx.coroutines.core)
    testFixturesImplementation(libs.androidx.ui)
    testFixturesImplementation(platform(libs.androidx.compose.bom))
}

afterEvaluate {
    val moduleNamespace = project.extensions.findByType(BaseExtension::class.java)?.namespace
        ?: project.extensions.findByType(BaseExtension::class.java)?.defaultConfig?.applicationId.orEmpty()

    project.extensions.findByName("ksp")?.let { kspExt ->
        if (kspExt is com.google.devtools.ksp.gradle.KspExtension) {
            kspExt.arg("com.mercari.naventryscope.package", moduleNamespace)
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
