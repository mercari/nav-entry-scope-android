import org.gradle.api.publish.PublishingExtension
import org.gradle.plugins.signing.SigningExtension

plugins.withId("maven-publish") {
    plugins.withId("signing") {
        val publishing = extensions.getByType<PublishingExtension>()
        val signing = extensions.getByType<SigningExtension>()

        val signingKey = System.getenv("SIGNING_KEY")
        val signingPassword = System.getenv("SIGNING_PASSWORD")

        if (signingKey != null && signingPassword != null) {
            signing.useInMemoryPgpKeys(signingKey, signingPassword)
            signing.sign(publishing.publications)
        }
    }
}
