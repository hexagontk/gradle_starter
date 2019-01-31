package org.example

import java.time.LocalDateTime.now

import com.hexagonkt.http.httpDate
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager.bindObject
import com.hexagonkt.injection.InjectionManager.inject

val server: Server by lazy {
    Server {
        before {
            response.addHeader("Server", "Servlet/3.1")
            response.addHeader("Transfer-Encoding", "chunked")
            response.addHeader("Date", httpDate(now()))
        }

        get("/text") { ok("Hello, World!", "text/plain") }
    }
}

private fun Server(block: Router.() -> Unit): Server = Server(inject(), block = block)

/**
 * Start the service from the command line.
 */
fun main() {
    bindObject<ServerPort>(JettyServletAdapter())
    server.run()
}

