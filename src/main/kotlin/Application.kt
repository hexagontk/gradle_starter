package org.example

import com.hexagonkt.core.*
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.core.media.TEXT_PLAIN
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.model.Header

internal val settings = HttpServerSettings(ALL_INTERFACES, 9090)
internal val serverAdapter = JettyServletAdapter()

internal lateinit var server: HttpServer

internal fun main() {
    server = serve(serverAdapter, settings) {
        before("*") {
            send(headers = response.headers + Header("server", "Hexagon/4"))
        }

        get("/text") {
            ok("Hello, World!", contentType = ContentType(TEXT_PLAIN))
        }
    }
}
