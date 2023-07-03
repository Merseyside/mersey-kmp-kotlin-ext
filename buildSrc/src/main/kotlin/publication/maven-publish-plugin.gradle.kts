import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    val projectGitUrl = "https://github.com/Merseyside/mersey-kmp-kotlin-ext"

    pom {
        name.set("Mersey kotlin extensions")
        description.set("Multiplatform kotlin extensions library")
        url.set("https://github.com/Merseyside/mersey-kmp-kotlin-ext")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("Merseyside")
                name.set("Ivan Sablin")
                email.set("ivanklessablin@gmail.com")
            }
        }

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        issueManagement {
            system.set("GitHub")
            url.set("$projectGitUrl/issues")
        }

        scm {
            connection.set("scm:git:$projectGitUrl")
            developerConnection.set("scm:git:$projectGitUrl")
            url.set(projectGitUrl)
        }
    }

    publishToMavenCentral(SonatypeHost.S01)
}

//signing {
//    val inMemoryKey = project.findProperty("signingInMemoryKey")?.toString()?.let { base64Key ->
//        val _base = base64Key.replace("\n", "")
//        String(Base64.getDecoder().decode(_base))
//    }
//    if (inMemoryKey != null) {
//        val inMemoryKeyId = project.findProperty("signingInMemoryKeyId")?.toString()
//        val inMemoryKeyPassword = project.findProperty("signingInMemoryKeyPassword")?.toString()
//
//        useInMemoryPgpKeys(inMemoryKeyId, inMemoryKey, inMemoryKeyPassword)
//        sign(publishing.publications)
//    }

//    val signingKeyId: String? = project.getProperty("ORG_GRADLE_PROJECT_signingInMemoryKeyId")
//    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
//    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
//        val _base = base64Key.replace("\n", "")
//        String(Base64.getDecoder().decode(_base))
//    }
//    if (signingKeyId != null) {
//        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
//        sign(publishing.publications)
//    }
//}

