package com.alexleventer.slack.utils

import java.io.File
import java.util.concurrent.TimeUnit

class GitUtil(private val workingDir: File) {

    fun lastCommitAuthorName(): String? = run("git", "log", "-1", "--format=%an")

    fun lastCommitAuthorEmail(): String? = run("git", "log", "-1", "--format=%ae")

    fun lastCommitMessage(): String? = run("git", "log", "-1", "--format=%B")

    private fun run(vararg command: String): String? = try {
        val proc = ProcessBuilder(*command)
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        if (!proc.waitFor(10, TimeUnit.SECONDS)) {
            proc.destroy()
            null
        } else {
            proc.inputStream.bufferedReader().readText().trim().ifEmpty { null }
        }
    } catch (_: Exception) {
        null
    }
}
