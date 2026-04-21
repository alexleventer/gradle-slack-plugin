package com.alexleventer.slack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.build.event.BuildEventsListenerRegistry
import javax.inject.Inject

abstract class SlackPlugin : Plugin<Project> {
    @get:Inject
    abstract val registry: BuildEventsListenerRegistry

    override fun apply(project: Project) {
        val extension = project.extensions.create("slack", SlackExtension::class.java)

        val serviceProvider = project.gradle.sharedServices.registerIfAbsent(
            "gradleSlackPlugin",
            SlackBuildService::class.java
        ) { spec ->
            spec.parameters.webhookUrl.set(extension.webhookUrl)
            spec.parameters.username.set(extension.username)
            spec.parameters.iconUrl.set(extension.iconUrl)
            spec.parameters.introText.set(extension.introText)
            spec.parameters.shouldMonitor.set(extension.shouldMonitor)
            spec.parameters.projectDir.set(project.rootDir.absolutePath)
        }

        registry.onTaskCompletion(serviceProvider)
    }
}
