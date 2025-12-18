import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    jacoco
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mercari"
            artifactId = "nav-entry-scope-processor"
            version = "1.0.0"

            from(components["java"])
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(true)
    }
}