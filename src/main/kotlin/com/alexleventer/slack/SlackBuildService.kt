package com.alexleventer.slack

import com.alexleventer.slack.utils.GitUtil
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.task.TaskFailureResult
import org.gradle.tooling.events.task.TaskFinishEvent
import java.io.File

abstract class SlackBuildService : BuildService<SlackBuildService.Params>, OperationCompletionListener {

    interface Params : BuildServiceParameters {
        val webhookUrl: Property<String>
        val username: Property<String>
        val iconUrl: Property<String>
        val introText: Property<String>
        val shouldMonitor: ListProperty<String>
        val projectDir: Property<String>
    }

    private val api: SlackApi by lazy { SlackApi(parameters.webhookUrl.get()) }
    private val gitUtil by lazy { GitUtil(File(parameters.projectDir.get())) }

    override fun onFinish(event: FinishEvent) {
        if (event !is TaskFinishEvent) return
        val taskName = event.descriptor.taskPath.substringAfterLast(":")
        if (taskName !in parameters.shouldMonitor.get()) return
        if (parameters.webhookUrl.orNull.isNullOrBlank()) return

        val success = event.result !is TaskFailureResult
        val message = SlackMessageBuilder(
            taskName = taskName,
            success = success,
            username = parameters.username.get(),
            iconUrl = parameters.iconUrl.get(),
            introText = parameters.introText.get(),
            git = gitUtil
        ).build()

        runCatching { api.postMessage(message) }
    }
}
