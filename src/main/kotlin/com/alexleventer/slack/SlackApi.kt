package com.alexleventer.slack

import com.alexleventer.slack.utils.HTTPUtil

open class SlackApi(private val webhookUrl: String) {
    private var client = HTTPUtil()

    fun createMessage(json: String): String {
        return client.post(webhookUrl, json)
    }
}
