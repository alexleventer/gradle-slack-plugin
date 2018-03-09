package com.alexleventer.slack

import com.alexleventer.slack.utils.GitUtils
import org.gradle.api.tasks.TaskState
import org.gradle.api.Task
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.Instant

open class SlackMessageBuilder {
    private var task: Task
    private var taskState: TaskState
    private var consoleOutput: String
    private var extension: SlackExtension
    private var json: JsonObject = JsonObject()
    constructor(task: org.gradle.api.Task, taskState: TaskState, consoleOutput: String, extension: SlackExtension) {
        this.task = task
        this.taskState = taskState
        this.consoleOutput = consoleOutput
        this.extension = extension
    }

    fun buildSlackMessageJSONBody(): String {
        val failure: Throwable? = taskState.failure
        val success: Boolean = failure === null
        val status = if (success) "Success" else "Failure"
        val attachmentColor = if (success) "good" else "danger"
        val graphephantPath = "https://raw.githubusercontent.com/alexleventer/gradle-slack-plugin/master/gradlephant.png"
        val authorAvatar = "https://avatars.io/gravatar/${GitUtils.getLastCommitAuthorEmail()}"

        json.addProperty("text", "Your Gradle Build is Complete:")
        json.addProperty("username", extension.username)
        json.addProperty("icon_url", graphephantPath)
        json.addProperty("mrkdwn", true)

        val attachments = JsonArray()
        val buildAttachment = JsonObject()
        buildAttachment.addProperty("color", attachmentColor)
        buildAttachment.addProperty("footer", "Gradle Build")
        buildAttachment.addProperty("author_name", GitUtils.getLastCommitAuthorName())
        buildAttachment.addProperty("title", GitUtils.getLastCommitMessage())
        buildAttachment.addProperty("title_link", "")
        buildAttachment.addProperty("author_icon", authorAvatar)
        buildAttachment.addProperty("author_link", "")
        buildAttachment.addProperty("footer_icon", graphephantPath)
        buildAttachment.addProperty("ts", Instant.now().epochSecond)

        val fields = JsonArray()
        val taskField = JsonObject()
        taskField.addProperty("title", "Task")
        taskField.addProperty("value", this.task.name)
        taskField.addProperty("short", true)

        val statusField = JsonObject()
        statusField.addProperty("title", "Status")
        statusField.addProperty("value", status)
        statusField.addProperty("short", true)
        fields.add(taskField)
        fields.add(statusField)

        buildAttachment.add("fields", fields)
        attachments.add(buildAttachment)
        json.add("attachments", attachments)

        return json.toString()
    }
}
