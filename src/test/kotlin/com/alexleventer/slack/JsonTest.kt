package com.alexleventer.slack

import com.alexleventer.slack.utils.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JsonTest {

    @Test
    fun `encodes primitives and escapes strings`() {
        assertThat(Json.encode(null)).isEqualTo("null")
        assertThat(Json.encode(true)).isEqualTo("true")
        assertThat(Json.encode(42)).isEqualTo("42")
        assertThat(Json.encode("line\nbreak\"quote")).isEqualTo("\"line\\nbreak\\\"quote\"")
    }

    @Test
    fun `encodes nested maps and lists`() {
        val payload = mapOf(
            "text" to "hello",
            "items" to listOf(1, 2, mapOf("k" to "v"))
        )
        assertThat(Json.encode(payload))
            .isEqualTo("""{"text":"hello","items":[1,2,{"k":"v"}]}""")
    }
}
