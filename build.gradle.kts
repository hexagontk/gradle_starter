import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import java.lang.System.getProperty

plugins {
    kotlin("jvm") version("1.8.10")
    id("org.graalvm.buildtools.native") version("0.9.20")
}

val hexagonVersion = "2.6.3"
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
        getProperty(name)?.let(value).also { println("$name : $it") }

    binaries {
        named("main") {
            listOfNotNull(
                "--enable-url-protocols=classpath",
                "--initialize-at-run-time=com.hexagonkt.core.NetworkKt",
                "--initialize-at-build-time=com.hexagonkt.core.ClasspathHandler",
                option("static") { "--static" },
                option("enableMonitoring") { "--enable-monitoring" },
                option("mostlyStatic") { "-H:+StaticExecutableWithDynamicLibC" },
                option("heap") { "-R:MaxHeapSize=$it" },
            )
            .forEach(buildArgs::add)
        }
    }
}
