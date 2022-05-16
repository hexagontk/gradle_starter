
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

tasks.register<Exec>("nativeImage") {
    dependsOn("jarAll")

    commandLine(
        listOf(
            "native-image",
            "--no-fallback",
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

            "-jar",
            "$buildDir/libs/${project.name}-all-${project.version}.jar",
            "$buildDir/${project.name}",
        )
    )
}

tasks.register<Exec>("upx") {
    dependsOn("nativeImage")

    commandLine(listOf("upx", "$buildDir/${project.name}"))
}

// Should be executed in integration tests (application running in its own VM). To avoid including
// tests classpath into image configuration. Check:
// www.graalvm.org/reference-manual/native-image/BuildConfiguration/#embedding-a-configuration-file
// on how to add Native Image config to libraries' jars (using guid/aid in META-INF)
tasks.register<Exec>("agentRun") {
    dependsOn("jarAll")

    val dir="$buildDir/resources/main"
    val configOutput="config-output-dir=$dir/META-INF/native-image"

    commandLine(listOf(
        "java",
        "-agentlib:native-image-agent=$configOutput",
        "-jar",
        "$buildDir/libs/${project.name}-all-${project.version}.jar",
        "$buildDir/${project.name}"
    ))
}
