package org.example

import com.hexagonkt.core.media.TEXT_PLAIN
import com.hexagonkt.http.client.HttpClient
import com.hexagonkt.http.client.HttpClientSettings
import com.hexagonkt.http.client.jetty.JettyClientAdapter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(PER_CLASS)
internal class ApplicationTest {

    private val client by lazy {
        val clientSettings = HttpClientSettings(URL("http://localhost:${server.runtimePort}"))
        HttpClient(JettyClientAdapter(), clientSettings)
    }

    @BeforeAll fun beforeAll() {
        main()
        client.start()
    }

    @AfterAll fun afterAll() {
        client.stop()
        server.stop()
    }

    @Test fun `HTTP request returns proper status, headers and body`() {
        val response = client.get("/text")
        val content = response.body

        assertNotNull(response.headers["server"])
        assertEquals(TEXT_PLAIN, response.contentType?.mediaType)

        assertEquals("Hello, World!", content)
    }
}
