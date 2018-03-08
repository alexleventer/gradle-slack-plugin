package com.alexleventer.slack

import org.gradle.api.Project

open class SlackExtension(val project: Project) {
    var webhookUrl: String = ""
    var username: String = ""
    var shouldMonitor: List<Any> = mutableListOf()

    fun shouldMonitor(vararg task: Any) {
        this.shouldMonitor = task.asList()
    }
}
