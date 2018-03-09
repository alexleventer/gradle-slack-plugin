import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.gradle.api.tasks.SourceSet
import java.net.HttpURLConnection

group = "com.alexleventer.gradle"
version = "1.0.3"

apply {
    plugin("java")
    plugin("kotlin")
}

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.2.30"

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlinVersion))
        classpath("com.squareup.okhttp3:okhttp:3.10.0")
        classpath("com.google.code.gson:gson:2.8.2")
        classpath(gradleApi())
    }
}

plugins {
    java
    id("com.gradle.plugin-publish") version "0.9.7"
}

java.sourceSets["test"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir(file("src/test/kotlin"))
}

val kotlinVersion: String by extra

repositories {
    jcenter()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlinVersion))
    compile("com.squareup.okhttp3:okhttp:3.10.0")
    compile("com.google.code.gson:gson:2.8.2")
    implementation(gradleApi())

    testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testCompile("junit:junit:4.12")
    testCompile("org.mockito:mockito-core:2.10.0")
    testCompile("org.assertj:assertj-core:3.8.0")
    testCompile("com.nhaarman:mockito-kotlin-kt1.1:1.5.0")
    testRuntime(gradleKotlinDsl())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

pluginBundle {
    website = "https://github.com/alexleventer/gradle-slack-plugin"
    vcsUrl = "https://github.com/alexleventer/gradle-slack-plugin"
    description = "Post messages to Slack after your Gradle builds complete"
    tags = mutableListOf("gradle", "slack", "kotlin")

    plugins.create("Gradle Slack Plugin") {
        id = "com.alexleventer.slack"
        displayName = "Gradle Slack Plugin"
    }
}