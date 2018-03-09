package com.alexleventer.slack

open class SlackExtension {
    var webhookUrl: String = ""
    var username: String = ""
    var shouldMonitor: List<Any> = mutableListOf()

    fun shouldMonitor(vararg task: Any) {
        this.shouldMonitor = task.asList()
    }
}
