import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.maven.publish)
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

extra["navEntryScopeVersion"] = libs.versions.navEntryScope.get()
apply(from = rootProject.file("gradle/publishing.gradle.kts"))

@Suppress("UNCHECKED_CAST")
val configurePom = extra["configurePom"] as (org.gradle.api.publish.maven.MavenPom) -> Unit

mavenPublishing {
    configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = true))
    publishToMavenCentral()
    signAllPublications()

    coordinates("com.mercari", "nav-entry-scope-processor", extra["publishVersion"] as String)

    pom {
        name.set("NavEntryScope Processor")
        description.set("KSP annotation processor for NavEntryScope - generates scoped ViewModel factories for Jetpack Compose navigation")
        configurePom(this)
    }
}
