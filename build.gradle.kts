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
    mainClass.set("org.example.ApplicationKt")
}

dependencies {
    "implementation"("com.hexagonkt:http_server_jetty:$hexagonVersion")
    "implementation"("org.slf4j:slf4j-nop:2.0.6")

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

tasks.register<Exec>("dockerBuild") {
    dependsOn("build", "tarNative", "tarJpackage")
    file("build/jpackage").deleteRecursively()
    commandLine("docker-compose build --build-arg PROJECT=${project.name}".split(" "))
}
