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
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

abstract class SlackBuildService :
    BuildService<SlackBuildService.Params>, OperationCompletionListener, AutoCloseable {

    interface Params : BuildServiceParameters {
        val webhookUrl: Property<String>
        val username: Property<String>
        val iconUrl: Property<String>
        val introText: Property<String>
        val shouldMonitor: ListProperty<String>
        val notifyOnBuildFinished: Property<Boolean>
        val projectDir: Property<String>
    }

    private val api: SlackApi by lazy { SlackApi(parameters.webhookUrl.get()) }
    private val gitUtil by lazy { GitUtil(File(parameters.projectDir.get())) }

    private val anyFailure = AtomicBoolean(false)
    private val taskCount = AtomicInteger(0)
    private val failedCount = AtomicInteger(0)

    override fun onFinish(event: FinishEvent) {
        if (event !is TaskFinishEvent) return
        val taskName = event.descriptor.taskPath.substringAfterLast(":")
        val success = event.result !is TaskFailureResult

        if (parameters.notifyOnBuildFinished.getOrElse(false)) {
            taskCount.incrementAndGet()
            if (!success) {
                anyFailure.set(true)
                failedCount.incrementAndGet()
            }
            return
        }

        if (taskName !in parameters.shouldMonitor.get()) return
        if (parameters.webhookUrl.orNull.isNullOrBlank()) return

        val message = SlackMessageBuilder.forTask(
            taskName = taskName,
            success = success,
            username = parameters.username.get(),
            iconUrl = parameters.iconUrl.get(),
            introText = parameters.introText.get(),
            git = gitUtil
        ).build()

        runCatching { api.postMessage(message) }
    }

    override fun close() {
        if (!parameters.notifyOnBuildFinished.getOrElse(false)) return
        if (parameters.webhookUrl.orNull.isNullOrBlank()) return
        if (taskCount.get() == 0) return

        val message = SlackMessageBuilder.forBuild(
            success = !anyFailure.get(),
            totalTasks = taskCount.get(),
            failedTasks = failedCount.get(),
            username = parameters.username.get(),
            iconUrl = parameters.iconUrl.get(),
            introText = parameters.introText.get(),
            git = gitUtil
        ).build()

        runCatching { api.postMessage(message) }
    }
}
