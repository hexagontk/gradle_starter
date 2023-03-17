import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import java.lang.System.getProperty

plugins {
    kotlin("jvm") version("1.8.10")
    id("org.graalvm.buildtools.native") version("0.9.20")
}

val hexagonVersion = "2.6.4"
val gradleScripts = "https://raw.githubusercontent.com/hexagonkt/hexagon/$hexagonVersion/gradle"

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")
apply(from = "$gradleScripts/native.gradle")

defaultTasks("build")

version="1.0.0"
group="org.example"
description="Service's description"

extensions.configure<JavaApplication> {
    mainClass.set("org.example.GradleStarterKt")
}

dependencies {
    "implementation"("com.hexagonkt:http_server_netty:$hexagonVersion")

    "testImplementation"("com.hexagonkt:http_client_jetty:$hexagonVersion")
}

extensions.configure<GraalVMExtension> {
    fun option(name: String, value: (String) -> String): String? =
        getProperty(name)?.let(value)

    binaries {
        named("main") {
            listOfNotNull(
                "--enable-url-protocols=classpath",
                "--initialize-at-run-time=com.hexagonkt.core.NetworkKt",
                "--initialize-at-build-time=com.hexagonkt.core.ClasspathHandler",
                "--static", // Won't work on Windows or macOS
                option("enableMonitoring") { "--enable-monitoring" },
                option("heap") { "-R:MaxHeapSize=$it" },
            )
            .forEach(buildArgs::add)
        }
    }
}

tasks.register("setUp") {
    doLast {
    }
}

tasks.register<Exec>("dockerCreate") {
    dependsOn("build", "tarNative", "tarJpackage")
    commandLine("docker-compose create --build --force-recreate".split(" "))
}

tasks.register<Exec>("dockerUp") {
    dependsOn("dockerCreate")
    commandLine("docker-compose up -d".split(" "))
}

tasks.register<Exec>("dockerImages") {
    dependsOn("dockerCreate")
    commandLine("docker-compose images".split(" "))
}

tasks.register<Exec>("dockerStats") {
    dependsOn("dockerUp")
    commandLine("docker stats --no-stream".split(" "))
}
