package org.example

import com.hexagonkt.core.ALL_INTERFACES
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.core.logging.LoggingManager
import com.hexagonkt.core.media.TEXT_PLAIN
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.model.Header

internal val settings = HttpServerSettings(ALL_INTERFACES, 9090)
internal val serverAdapter = JettyServletAdapter(minThreads = 4)

internal lateinit var server: HttpServer

internal fun main() {
    LoggingManager.defaultLoggerName = "org.example"
    server = serve(serverAdapter, settings) {
        on("*") {
            send(headers = response.headers + Header("server", "Hexagon/2.6"))
        }

        get("/text") {
            ok("Hello, World!", contentType = ContentType(TEXT_PLAIN))
        }
    }
}
