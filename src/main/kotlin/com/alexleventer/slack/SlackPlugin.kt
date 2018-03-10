package com.alexleventer.slack

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState

open class SlackPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val extension:SlackExtension = project.extensions.create("slack", SlackExtension::class.java)
        project.afterEvaluate {
            project.gradle.taskGraph.addTaskExecutionListener(TaskExecutionListener(extension))
        }
    }

    class TaskExecutionListener(private var extension: SlackExtension) : org.gradle.api.execution.TaskExecutionListener {
        override fun beforeExecute(task: Task) {}

        override fun afterExecute(task: Task, state: TaskState) {
            val slack = SlackApi(extension.webhookUrl)
            if (isMonitored(task)) {
                val message = SlackMessageBuilder(task, state, extension)
                slack.createMessage(message.buildSlackMessageJSONBody())
            }
        }

        private fun isMonitored(task: Task): Boolean {
            if (task.name in extension.shouldMonitor) {
                return true
            }
            return false
        }
    }
}

