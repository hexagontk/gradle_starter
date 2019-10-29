package org.example

import com.hexagonkt.helpers.logger
import java.time.LocalDateTime.now

import com.hexagonkt.http.httpDate
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager.bindObject

val server: Server by lazy {
    Server {
        before {
            response.setHeader("Server", "Servlet/3.1")
            response.setHeader("Transfer-Encoding", "chunked")
            response.setHeader("Date", httpDate(now()))
        }

        get("/text") { ok("Hello, World!", "text/plain") }
    }
}

/**
 * Start the service from the command line.
 */
fun main() {
    bindObject<ServerPort>(JettyServletAdapter())
    server.start()
    logger.info { "Application started" }
}
