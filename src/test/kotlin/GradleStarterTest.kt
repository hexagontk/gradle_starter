package org.example

import com.hexagonkt.http.client.Client
import com.hexagonkt.http.client.ahc.AhcAdapter
import io.kotest.core.spec.style.StringSpec

class GradleStarterTest : StringSpec({

    val client by lazy { Client(AhcAdapter(), "http://localhost:${server.runtimePort}") }

    beforeSpec {
        main()
    }

    afterSpec {
        server.stop()
    }

    "HTTP request returns proper status, headers and body" {
        val response = client.get("/text")
        val content = response.body

        assert(response.headers["Date"] != null)
        assert(response.headers["Server"] != null)
        assert(response.headers["Transfer-Encoding"] != null)
        assert(response.headers["Content-Type"]?.first() == "text/plain")

        assert("Hello, World!" == content)
    }
})
