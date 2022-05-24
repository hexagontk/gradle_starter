
plugins {
    kotlin("jvm") version("1.6.21")
    id("org.jetbrains.dokka") version("1.6.21")
    id("org.graalvm.buildtools.native") version("0.9.11")
}

val gradleScripts = properties["gradleScripts"]
val hexagonVersion = properties["hexagonVersion"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")

extensions.configure<JavaApplication> {
    mainClass.set("org.example.GradleStarterKt")
}

dependencies {
    implementation("com.hexagonkt:http_server_netty:$hexagonVersion")
    implementation("com.hexagonkt:logging_slf4j_jul:$hexagonVersion")

    testImplementation("com.hexagonkt:http_client_jetty:$hexagonVersion")
}

graalvmNative {
    binaries {
        named("main") {
            listOf(
                "--enable-http",
                "--enable-https",
                "--enable-url-protocols=classpath",
                "--initialize-at-build-time=com.hexagonkt.core.ClasspathHandler",

                // Netty options (not needed for Jetty)
                "--initialize-at-build-time=org.slf4j.LoggerFactory",
                "--initialize-at-build-time=org.slf4j.impl.JDK14LoggerAdapter",
                "--initialize-at-build-time=org.slf4j.impl.StaticLoggerBinder",
                "--allow-incomplete-classpath",
                "--initialize-at-run-time=io.netty.internal.tcnative.AsyncSSLPrivateKeyMethod",
                "--initialize-at-run-time=io.netty.internal.tcnative.SSL",
                "--initialize-at-run-time=io.netty.internal.tcnative.CertificateVerifier",
                "--initialize-at-run-time=io.netty.handler.ssl.OpenSslPrivateKeyMethod",
                "--initialize-at-run-time=io.netty.handler.ssl.ReferenceCountedOpenSslEngine",
                "--initialize-at-run-time=io.netty.handler.ssl.OpenSslAsyncPrivateKeyMethod",
                "--initialize-at-run-time=io.netty.handler.ssl.JettyNpnSslEngine",
                "--initialize-at-run-time=io.netty.internal.tcnative.SSLPrivateKeyMethod",
                "--initialize-at-run-time=io.netty.handler.ssl.JdkNpnApplicationProtocolNegotiator",
                "--initialize-at-run-time=io.netty.handler.ssl.ConscryptAlpnSslEngine",
                "--initialize-at-run-time=org.bouncycastle.jsse.BCSSLEngine",
            )
            .forEach(buildArgs::add)
        }
    }
}

tasks.register<Exec>("upx") {
    dependsOn("nativeCompile")

    val command = "upx $buildDir/native/nativeCompile/${project.name} -o $buildDir/${project.name}"
    commandLine(command.split(" "))
}
