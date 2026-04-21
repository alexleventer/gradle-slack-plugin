package com.alexleventer.slack.utils

import java.security.MessageDigest

internal object Json {

    fun encode(value: Any?): String = buildString { encode(this, value) }

    fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun encode(sb: StringBuilder, value: Any?): StringBuilder = sb.apply {
        when (value) {
            null -> append("null")
            is Boolean, is Number -> append(value.toString())
            is CharSequence -> appendString(value.toString())
            is Map<*, *> -> appendMap(value)
            is Iterable<*> -> appendList(value)
            is Array<*> -> appendList(value.asList())
            else -> appendString(value.toString())
        }
    }

    private fun StringBuilder.appendMap(map: Map<*, *>) {
        append('{')
        var first = true
        for ((k, v) in map) {
            if (!first) append(',')
            first = false
            appendString(k.toString())
            append(':')
            encode(this, v)
        }
        append('}')
    }

    private fun StringBuilder.appendList(items: Iterable<*>) {
        append('[')
        var first = true
        for (item in items) {
            if (!first) append(',')
            first = false
            encode(this, item)
        }
        append(']')
    }

    private fun StringBuilder.appendString(s: String) {
        append('"')
        for (c in s) {
            when (c) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (c.code < 0x20) append("\\u%04x".format(c.code)) else append(c)
            }
        }
        append('"')
    }
}
