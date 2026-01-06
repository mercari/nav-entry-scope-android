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

extra["navEntryScopeVersion"] = libs.versions.navEntryScope.get()

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "nav-entry-scope-processor"
            pom {
                name.set("NavEntryScope Processor")
                description.set("KSP annotation processor for NavEntryScope - generates scoped ViewModel factories for Jetpack Compose navigation")
            }
        }
    }
}

apply(from = rootProject.file("gradle/publishing.gradle.kts"))