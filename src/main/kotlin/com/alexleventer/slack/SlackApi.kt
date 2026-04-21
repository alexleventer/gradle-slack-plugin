package com.alexleventer.slack

import com.alexleventer.slack.utils.HttpClientUtil

open class SlackApi(private val webhookUrl: String) {
    private val http = HttpClientUtil()

    fun postMessage(json: String): Int = http.postJson(webhookUrl, json)
}
