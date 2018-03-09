package com.alexleventer.slack.utils

import java.io.IOException
import java.util.concurrent.TimeUnit

class GitUtils {

    companion object {
        fun getLastCommitAuthorName(): String? {
            return "git log -1 --format=%an".runCommand()
        }

        fun getLastCommitAuthorEmail(): String? {
            return "git log -1 --format=%ae".runCommand()
        }

        fun getLastCommitMessage(): String? {
            return "git log -1 --format=%B".runCommand()
        }

        private fun String.runCommand(): String? {
            try {
                val parts = this.split("\\s".toRegex())
                val proc = ProcessBuilder(*parts.toTypedArray())
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .start()

                proc.waitFor(60, TimeUnit.MINUTES)
                return proc.inputStream.bufferedReader().readText()
            } catch(e: IOException) {
                e.printStackTrace()
                return null
            }
        }
    }
}