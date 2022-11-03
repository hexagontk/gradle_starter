package org.example

import com.hexagonkt.core.allInterfaces
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.netty.NettyServerAdapter
import com.hexagonkt.core.logging.LoggingManager
import com.hexagonkt.core.media.TextMedia.PLAIN
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.model.Header

internal val settings = HttpServerSettings(
    bindAddress = allInterfaces,
    bindPort = 9090
)

internal val server: HttpServer by lazy {
    HttpServer(NettyServerAdapter(), settings) {
        on("*") {
            send(headers = response.headers + Header("server", "Servlet/3.1"))
        }

        get("/text") {
            ok("Hello, World!", contentType = ContentType(PLAIN))
        }
    }
}

internal fun main() {
    LoggingManager.defaultLoggerName = "org.example"
    server.start()
}
