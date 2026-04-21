package com.alexleventer.slack

import com.alexleventer.slack.utils.GitUtil
import com.alexleventer.slack.utils.Json
import java.time.Instant

open class SlackMessageBuilder private constructor(
    private val title: String,
    private val success: Boolean,
    private val fields: List<Map<String, Any>>,
    private val username: String,
    private val iconUrl: String,
    private val introText: String,
    private val git: GitUtil
) {
    fun build(): String {
        val authorEmail = git.lastCommitAuthorEmail().orEmpty()
        val authorAvatar = "https://www.gravatar.com/avatar/${Json.md5(authorEmail.lowercase())}?d=identicon"

        val attachment = mapOf(
            "color" to if (success) "good" else "danger",
            "footer" to "Gradle Build",
            "footer_icon" to iconUrl,
            "author_name" to git.lastCommitAuthorName().orEmpty(),
            "author_icon" to authorAvatar,
            "title" to title,
            "ts" to Instant.now().epochSecond,
            "fields" to fields
        )

        val payload = mapOf(
            "text" to introText,
            "username" to username,
            "icon_url" to iconUrl,
            "mrkdwn" to true,
            "attachments" to listOf(attachment)
        )

        return Json.encode(payload)
    }

    companion object {
        fun forTask(
            taskName: String,
            success: Boolean,
            username: String,
            iconUrl: String,
            introText: String,
            git: GitUtil
        ) = SlackMessageBuilder(
            title = git.lastCommitMessage().orEmpty(),
            success = success,
            fields = listOf(
                mapOf("title" to "Task", "value" to taskName, "short" to true),
                mapOf("title" to "Status", "value" to if (success) "Success" else "Failure", "short" to true)
            ),
            username = username,
            iconUrl = iconUrl,
            introText = introText,
            git = git
        )

        fun forBuild(
            success: Boolean,
            totalTasks: Int,
            failedTasks: Int,
            username: String,
            iconUrl: String,
            introText: String,
            git: GitUtil
        ) = SlackMessageBuilder(
            title = git.lastCommitMessage().orEmpty(),
            success = success,
            fields = listOf(
                mapOf("title" to "Status", "value" to if (success) "Success" else "Failure", "short" to true),
                mapOf("title" to "Tasks", "value" to "$totalTasks executed, $failedTasks failed", "short" to true)
            ),
            username = username,
            iconUrl = iconUrl,
            introText = introText,
            git = git
        )
    }
}
