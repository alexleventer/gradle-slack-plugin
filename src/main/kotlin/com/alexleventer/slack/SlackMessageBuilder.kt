package com.alexleventer.slack

import org.gradle.api.tasks.TaskState
import org.gradle.api.Task

open class SlackMessageBuilder {
    private var task : Task
    private var taskState : TaskState
    private var consoleOutput : String
    private var extension : SlackExtension
    constructor(task: org.gradle.api.Task, taskState: TaskState, consoleOutput: String, extension: SlackExtension) {
        this.task = task
        this.taskState = taskState
        this.consoleOutput = consoleOutput
        this.extension = extension
    }

    fun buildJSON() : String {
        val status = if(taskState.failure != null) "Failure" else "Success"
        val attachmentColor = if(status.equals("Failure")) "#e74c3c" else "#27ae60"
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
