import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    `maven-publish`
    signing
}

group = "com.sealwu"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            pom {
                name.set("Kscript Tools")
                description.set("Easy way run shell commandline in kotlin and scripts")
                url.set("http://github.com/wuseal/Kscript-Tools")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("wuseal")
                        name.set("wuseal")
                        email.set("wusealking@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/wuseal/Kscript-Tools")
                }
            }
            artifactId = "kscript-tools"
            from(components["java"])

        }
    }
    repositories {
        maven {
            name = "mavenCentral"
            // change URLs to point to your repos, e.g. http://my.org/repo
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val urlString = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            url = uri(urlString)
            credentials(PasswordCredentials::class)
        }
    }
}

signing {
    sign(publishing.publications.getByName("mavenKotlin"))
}