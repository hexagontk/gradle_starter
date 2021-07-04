package org.example

import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager
import com.hexagonkt.logging.LoggingPort
import com.hexagonkt.logging.Slf4jJulLoggingAdapter
import java.net.InetAddress

//import org.graalvm.nativeimage.hosted.RuntimeClassInitialization

internal val server: Server by lazy {
    Server {
        before {
            response.headers["Server"] = "Servlet/3.1"
        }

        get("/text") {
            ok("Hello, World!", "text/plain")
        }
    }
}

internal fun main() {
//    RuntimeClassInitialization.initializeAtBuildTime(ClasspathHandler::class.java)

    InjectionManager.module.apply {
        bind<LoggingPort>(Slf4jJulLoggingAdapter)
        bind<ServerPort>(JettyServletAdapter())
        // Bind to 0.0.0.0 is really slow on linux (check settings)
        bind(ServerSettings(bindPort = 9090, bindAddress = InetAddress.getByName("0.0.0.0")))
    }

    System.setProperty("com.hexagonkt.noJmx", "no")
    server.start()
}
