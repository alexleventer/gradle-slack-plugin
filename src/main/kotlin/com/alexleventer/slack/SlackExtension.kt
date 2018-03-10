package com.alexleventer.slack

open class SlackExtension {
    var webhookUrl: String = ""
    var username: String? = null
    var iconUrl: String? = null
    var introText: String? = null
    var shouldMonitor: List<Any> = mutableListOf()

    fun shouldMonitor(vararg task: Any) {
        this.shouldMonitor = task.asList()
    }
}
