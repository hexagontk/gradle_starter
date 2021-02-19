package org.example

import com.hexagonkt.helpers.logger
import com.hexagonkt.http.server.*
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager
import com.hexagonkt.logging.LoggingPort
import com.hexagonkt.logging.Slf4jLoggingAdapter
import java.net.InetAddress

internal val injector by lazy {
    InjectionManager.apply {
        bind<LoggingPort>(Slf4jLoggingAdapter)
        bind<ServerPort>(JettyServletAdapter())
        bind(ServerSettings(bindPort = 9090, bindAddress = InetAddress.getByName("0.0.0.0")))
    }
}

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
    logger.info { injector }
    server.start()
}

/*
$h/Software/graalvm_21_jdk11/bin/native-image \
  --initialize-at-run-time=com.hexagonkt.logging.jul.JulLoggingAdapter \
  --initialize-at-run-time=org.example.GradleStarterKt \
  --initialize-at-run-time=com.hexagonkt.ClasspathHandler \
  --initialize-at-run-time=kotlin.jvm.internal.Reflection \
  --initialize-at-run-time=com.hexagonkt.injection.InjectionManager \
  --initialize-at-run-time=com.hexagonkt.logging.LoggingManager \
  --initialize-at-run-time=com.hexagonkt.logging.Logger \
  --initialize-at-run-time=kotlin.text.Regex \
  --initialize-at-run-time=kotlin.jvm.internal.CallableReference \
  --initialize-at-run-time=com.hexagonkt.logging.jul.JulLoggingAdapter \
  --initialize-at-run-time=com.hexagonkt.ClasspathHandler \
  -jar build/libs/gradle_starter-all-0.1.0.jar
 */
