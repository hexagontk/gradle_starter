package org.example.stress

import com.hexagonkt.core.media.TEXT_PLAIN
import com.hexagonkt.http.client.HttpClient
import com.hexagonkt.http.client.jetty.JettyClientAdapter
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

fun main() {
    val client = HttpClient(JettyClientAdapter(), URL("http://localhost:9930")).apply { start() }

    while (true) {
        val response = client.get("/text")
        val content = response.body

        assertNotNull(response.headers["server"])
        assertEquals(TEXT_PLAIN, response.contentType?.mediaType)

        assertEquals("Hello, World!", content)
    }
}
