package org.example

import com.hexagonkt.http.client.Client
import com.hexagonkt.http.client.ahc.AhcAdapter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(PER_CLASS)
class MavenStarterTest {

    private val client by lazy { Client(AhcAdapter(), "http://localhost:${server.runtimePort}") }

    @BeforeAll
    fun beforeSpec() {
        main()
    }

    @AfterAll
    fun afterSpec() {
        server.stop()
    }

    @Test fun `HTTP request returns proper status, headers and body`() {
        val response = client.get("/text")
        val content = response.body

        assertNotNull(response.headers["Server"])
        assertEquals(response.headers["Content-Type"]?.first(), "text/plain")

        assertEquals("Hello, World!", content)
    }
}
