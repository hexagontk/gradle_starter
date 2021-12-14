package org.example

import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.core.logging.LoggingManager
import com.hexagonkt.logging.slf4j.jul.Slf4jJulLoggingAdapter

internal val server: Server by lazy {
    // Bind to 0.0.0.0 is really slow on linux (check settings)
    Server(JettyServletAdapter(), ServerSettings(bindPort = 9090)) {
        before {
            response.headers["Server"] = "Servlet/3.1"
        }

        get("/text") {
            ok("Hello, World!", "text/plain")
        }
    }
}

internal fun main() {
    LoggingManager.adapter = Slf4jJulLoggingAdapter
    server.start()
}
