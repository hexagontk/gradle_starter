package org.example

import com.hexagonkt.http.client.Client
import com.hexagonkt.http.client.ahc.AhcAdapter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

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

        response.headers["Date"].shouldNotBeNull()
        response.headers["Server"].shouldNotBeNull()
        response.headers["Transfer-Encoding"].shouldNotBeNull()
        response.headers["Content-Type"]?.first() shouldBe "text/plain"

        "Hello, World!" shouldBe content
    }
})
