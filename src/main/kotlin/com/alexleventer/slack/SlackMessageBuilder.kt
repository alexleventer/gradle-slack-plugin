package com.alexleventer.slack

import org.gradle.api.tasks.TaskState
import org.gradle.api.Task

open class SlackMessageBuilder {
    private var task: Task
    private var taskState: TaskState
    private var consoleOutput: String
    private var extension: SlackExtension
    constructor(task: org.gradle.api.Task, taskState: TaskState, consoleOutput: String, extension: SlackExtension) {
        this.task = task
        this.taskState = taskState
        this.consoleOutput = consoleOutput
        this.extension = extension
    }

    fun buildJSON(): String {
        val failure: Throwable? = taskState.failure
        val success: Boolean = failure === null
        val status = if(success) "Success" else "Failure"
        val attachmentColor = if(success) "#27ae60" else "#e74c3c"

        return """{
            "text": "Your Gradle Build is Complete:",
            "username" : "${extension.username}",
            "attachments" : [{
                "color": "$attachmentColor",
                "fields": [
                    {
                        "title": "Task",
                        "value": "${this.task.name}",
                        "short": true
                    },
                    {
                        "title": "Status",
                        "value": "$status",
                        "short": true
                    }

                ],
                "footer": "Gradle Build Tool"
            }]
        }"""
    }
}
