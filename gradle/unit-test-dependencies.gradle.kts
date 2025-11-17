/**
 * Shared test configuration using version catalog
 * 
 * Usage in build.gradle.kts:
 * apply(from = rootProject.file("gradle/unit-test-dependencies.gradle.kts"))
 */

tasks.withType<Test> {
    useJUnitPlatform()
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    val testImplementation by configurations
    val testRuntimeOnly by configurations

    // Junit5
    testImplementation(platform(libs.findLibrary("junit-bom").get()))
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())

    // Junit4 compatibility
    testImplementation(libs.findLibrary("junit").get())
    testRuntimeOnly(libs.findLibrary("junit-vintage-engine").get())

    // Coroutines
    testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())

    // Assertions
    testImplementation(kotlin("test"))
}

