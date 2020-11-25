package org.example

import com.hexagonkt.helpers.logger
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager
import java.net.InetAddress

internal val injector = InjectionManager.apply {
    bind<ServerPort>(JettyServletAdapter())
    bind(ServerSettings(bindPort = 9090, bindAddress = InetAddress.getByName("0.0.0.0")))
}

internal val server: Server = Server {
    before {
        response.headers["Server"] = "Servlet/3.1"
    }

    get("/text") {
        ok("Hello, World!", "text/plain")
    }
}

internal fun main() {
    logger.info { injector }
    server.start()
}
