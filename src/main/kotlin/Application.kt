package org.example

import com.hexagontk.core.*
import com.hexagontk.http.server.*
import com.hexagontk.http.server.jdk.JdkHttpServer
import com.hexagontk.core.media.TEXT_PLAIN
import com.hexagontk.http.model.ContentType
import com.hexagontk.http.model.Field

internal val settings = HttpServerSettings(ALL_INTERFACES, 9090)
internal val serverAdapter = JdkHttpServer()

internal lateinit var server: HttpServer

internal fun main() {
    server = serve(serverAdapter, settings) {
        before("*") {
            send(headers = response.headers + Field("server", "Hexagon/4"))
        }

        get("/text") {
            ok("Hello, World!", contentType = ContentType(TEXT_PLAIN))
        }
    }

    System.setProperty(HEXAGONTK_LOGGING_COLOR, "true")
    val banner = server.createBanner(Platform.uptime())
    logger.info { banner }
}
