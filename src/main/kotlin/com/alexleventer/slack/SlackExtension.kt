package com.alexleventer.slack

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class SlackExtension {
    abstract val webhookUrl: Property<String>
    abstract val username: Property<String>
    abstract val iconUrl: Property<String>
    abstract val introText: Property<String>
    abstract val shouldMonitor: ListProperty<String>
    abstract val notifyOnBuildFinished: Property<Boolean>

    init {
        username.convention("Gradle")
        iconUrl.convention(DEFAULT_ICON_URL)
        introText.convention("Your Gradle Build is Complete:")
        shouldMonitor.convention(emptyList())
        notifyOnBuildFinished.convention(false)
    }

    fun shouldMonitor(vararg tasks: String) {
        shouldMonitor.set(tasks.toList())
    }

    companion object {
        const val DEFAULT_ICON_URL =
            "https://raw.githubusercontent.com/alexleventer/gradle-slack-plugin/master/assets/gradlephant.png"
    }
}
