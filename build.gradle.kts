plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "com.alexleventer.gradle"
version = "2.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website.set("https://github.com/alexleventer/gradle-slack-plugin")
    vcsUrl.set("https://github.com/alexleventer/gradle-slack-plugin")

    plugins {
        create("slackPlugin") {
            id = "com.alexleventer.slack"
            implementationClass = "com.alexleventer.slack.SlackPlugin"
            displayName = "Gradle Slack Plugin"
            description = "Post messages to Slack after your Gradle builds complete."
            tags.set(listOf("slack", "notifications", "kotlin", "build"))
        }
    }
}
