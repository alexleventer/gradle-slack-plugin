package com.alexleventer.slack

open class SlackApi {
    private var webhookUrl:String
    private var client = HTTPUtils()

    constructor(webhookUrl:String) {
        this.webhookUrl = webhookUrl
    }

    fun createMessage(json: String): String {
        return client.post(webhookUrl, json)
    }
}
