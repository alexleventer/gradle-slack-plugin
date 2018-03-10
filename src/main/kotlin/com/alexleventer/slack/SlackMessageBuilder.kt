package com.alexleventer.slack

import com.alexleventer.slack.utils.GitUtil
import org.gradle.api.tasks.TaskState
import org.gradle.api.Task
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.Instant

open class SlackMessageBuilder(private var task: Task, private var taskState: TaskState,
                               private var consoleOutput: String, private var extension: SlackExtension) {
    private var json: JsonObject = JsonObject()

    fun buildSlackMessageJSONBody(): String {
        val success = buildSucceeded(taskState)

        val gradlephantPath = "https://raw.githubusercontent.com/alexleventer/gradle-slack-plugin/master/assets/gradlephant.png"
        val authorAvatar = "https://avatars.io/gravatar/${GitUtil.getLastCommitAuthorEmail()}"

        json.addProperty("text", if (extension.introText == null) "Your Gradle Build is Complete:" else extension.introText)
        json.addProperty("username", if (extension.username == null) "Gradle" else extension.username)
        json.addProperty("icon_url", if (extension.iconUrl == null) gradlephantPath else extension.iconUrl)
        json.addProperty("mrkdwn", true)

        val attachments = JsonArray()
        val buildAttachment = JsonObject()
        buildAttachment.addProperty("color", if (success) "good" else "danger")
        buildAttachment.addProperty("footer", "Gradle Build")
        buildAttachment.addProperty("author_name", GitUtil.getLastCommitAuthorName())
        buildAttachment.addProperty("title", GitUtil.getLastCommitMessage())
        buildAttachment.addProperty("title_link", "")
        buildAttachment.addProperty("author_icon", authorAvatar)
        buildAttachment.addProperty("author_link", "")
        buildAttachment.addProperty("footer_icon", gradlephantPath)
        buildAttachment.addProperty("ts", Instant.now().epochSecond)

        val fields = JsonArray()
        val taskField = JsonObject()
        taskField.addProperty("title", "Task")
        taskField.addProperty("value", this.task.name)
        taskField.addProperty("short", true)

        val statusField = JsonObject()
        statusField.addProperty("title", "Status")
        statusField.addProperty("value", if (success) "Success" else "Failure")
        statusField.addProperty("short", true)
        fields.add(taskField)
        fields.add(statusField)

        buildAttachment.add("fields", fields)
        attachments.add(buildAttachment)
        json.add("attachments", attachments)

        return json.toString()
    }

    private fun buildSucceeded(taskState: TaskState): Boolean {
        val failure: Throwable? = taskState.failure
        return failure === null
    }
}
