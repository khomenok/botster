plugins {
    application
    kotlin("jvm") version "1.5.10"
    id("maven-publish")
}

group = "com.botster"
version = "0.0.6"

repositories {
    mavenCentral()
}

val ktorVersion = "1.6.1"

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val githubPackagesDeveloper: String by project
val githubPackagesUserId: String by project
val githubPackagesToken: String by project

val pkgArtifactId: String = rootProject.name
val pkgArtifactGroup: String = project.group.toString()
val pkgArtifactVersion: String = project.version.toString()

val pkgGithubUsername = githubPackagesUserId
val pkgDeveloperName = githubPackagesDeveloper
val pkgGithubDescription = "Framework for bots creation"
val pkgGithubHttpUrl = "https://github.com/${pkgGithubUsername}/${pkgArtifactId}"
val pkgGithubIssueTrackerUrl = "https://github.com/${pkgGithubUsername}/${pkgArtifactId}/issues"
val myLicense = "Apache-2.0"
val myLicenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE.md") {
        into("META-INF")
    }
}

// More info on `publishing`:
//   https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:resolved_dependencies
// More info on authenticating with personal access token (myDeveloperId and myArtifactName must be lowercase):
//   https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#authenticating-to-github-packages
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${pkgGithubUsername}/${pkgArtifactId}")
            credentials {
                username = pkgGithubUsername
                password = githubPackagesToken
            }
        }
    }
}

publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            groupId = pkgArtifactGroup
            artifactId = pkgArtifactId
            version = pkgArtifactVersion

            from(components["java"])

            artifact(sourcesJar)

            pom {
                packaging = "jar"
                name.set(pkgArtifactId)
                description.set(pkgGithubDescription)
                url.set(pkgGithubHttpUrl)
                scm {
                    url.set(pkgGithubHttpUrl)
                }
                issueManagement {
                    url.set(pkgGithubIssueTrackerUrl)
                }
                licenses {
                    license {
                        name.set(myLicense)
                        url.set(myLicenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(pkgGithubUsername)
                        name.set(pkgDeveloperName)
                    }
                }
            }

        }
    }
}