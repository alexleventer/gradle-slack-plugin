package com.alexleventer.slack

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

open class SlackPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val extension:SlackExtension = project.extensions.create("slack", SlackExtension::class.java, project)
        project.afterEvaluate {
            project.gradle.taskGraph.addTaskExecutionListener(TaskExecutionListener(extension))
        }
    }
}

class TaskExecutionListener(extension: SlackExtension) : TaskExecutionListener {
    private val webhookUrl = extension.webhookUrl
    private val slackExtension : SlackExtension = extension
    private val shouldMonitor = extension.shouldMonitor
    private val consoleOutput = StringBuilder()

    override fun beforeExecute(task: Task) {
        task.logging.addStandardOutputListener {
            consoleOutput.append(it)
        }
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val slack = SlackApi(webhookUrl)
        if (isMonitored(task)) {
            val message = SlackMessageBuilder(task, state, consoleOutput.toString(), slackExtension)
            slack.createMessage(message.buildJSON())
        }
    }

    private fun isMonitored(task: Task): Boolean {
        if (task.name in shouldMonitor) {
            return true
        }
        return false
    }
}
