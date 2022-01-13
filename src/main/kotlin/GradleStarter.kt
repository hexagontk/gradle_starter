package org.example

import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.core.logging.LoggingManager
import com.hexagonkt.core.logging.info
import com.hexagonkt.core.media.TextMedia.PLAIN
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.logging.slf4j.jul.Slf4jJulLoggingAdapter
import java.net.URL

internal val server: HttpServer by lazy {
    // Bind to 0.0.0.0 is really slow on linux (check settings)
    HttpServer(JettyServletAdapter(), HttpServerSettings(bindPort = 9090)) {
        on("*") {
            send(headers = response.headers + ("server" to "Servlet/3.1"))
        }

        get("/text") {
            ok("Hello, World!", contentType = ContentType(PLAIN))
        }
    }
}

internal fun main() {
    LoggingManager.adapter = Slf4jJulLoggingAdapter()
    URL("classpath:META-INF/build.properties").readText().info("Build Properties\n")
    server.start()
}
