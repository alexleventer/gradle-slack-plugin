package com.alexleventer.slack

import com.alexleventer.slack.utils.HTTPUtils

open class SlackApi {
    private var webhookUrl:String
    private var client = HTTPUtils()

    constructor(webhookUrl: String) {
        this.webhookUrl = webhookUrl
    }

    fun createMessage(json: String): String {
        return client.post(webhookUrl, json)
    }
}
