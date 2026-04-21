package com.alexleventer.slack

import com.alexleventer.slack.utils.GitUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class SlackMessageBuilderTest {

    private val git = object : GitUtil(File(".")) {
        override fun lastCommitAuthorName() = "Jane Dev"
        override fun lastCommitAuthorEmail() = "jane@example.com"
        override fun lastCommitMessage() = "feat: add widgets"
    }

    @Test
    fun `forTask emits task name and status fields`() {
        val json = SlackMessageBuilder.forTask(
            taskName = "build",
            success = true,
            username = "Gradle",
            iconUrl = "http://icon",
            introText = "Done:",
            git = git
        ).build()

        assertThat(json).contains("\"title\":\"Task\"", "\"value\":\"build\"")
        assertThat(json).contains("\"value\":\"Success\"", "\"color\":\"good\"")
        assertThat(json).contains("\"title\":\"feat: add widgets\"")
    }

    @Test
    fun `forBuild emits aggregate counts and failure color`() {
        val json = SlackMessageBuilder.forBuild(
            success = false,
            totalTasks = 12,
            failedTasks = 3,
            username = "Gradle",
            iconUrl = "http://icon",
            introText = "Build finished:",
            git = git
        ).build()

        assertThat(json).contains("\"value\":\"Failure\"", "\"color\":\"danger\"")
        assertThat(json).contains("\"title\":\"Tasks\"", "\"value\":\"12 executed, 3 failed\"")
    }
}
