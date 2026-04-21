package com.alexleventer.slack.utils

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

open class HttpClientUtil {
    private val client: HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build()

    open fun postJson(url: String, json: String): Int {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.discarding())
        return response.statusCode()
    }
}
