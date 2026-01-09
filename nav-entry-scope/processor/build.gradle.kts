import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.mercari.java.publish)
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

mercariPublishing {
    groupId.set("com.mercari")
    artifactId.set("nav-entry-scope-processor")
    displayName.set("NavEntryScope Processor")
    description.set("KSP annotation processor for NavEntryScope - generates scoped ViewModel factories for Jetpack Compose navigation")
    projectUrl.set("https://github.com/mercari/nav-entry-scope-android")
    licenseUrl.set("https://github.com/mercari/nav-entry-scope-android/blob/main/LICENSE.txt")
    scmConnection.set("scm:git:git://github.com/mercari/nav-entry-scope-android.git")
    scmDeveloperConnection.set("scm:git:ssh://github.com/mercari/nav-entry-scope-android.git")
    scmUrl.set("https://github.com/mercari/nav-entry-scope-android")
    developer("bxttx", "Luca Bettelli", "l-bettelli@mercari.com")
    developer("rsfez", "Robin Sfez", "rsfez@mercari.com")
}
