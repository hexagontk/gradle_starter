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
    binaries {
        named("main") {
            val https =
                if (getProperty("enableHttps") == "true") "--enable-https"
                else null
            val monitoring =
                if (getProperty("enableMonitoring") == "true") "--enable-monitoring"
                else null
            val mostlyStatic =
                if (getProperty("mostlyStatic") == "true") "-H:+StaticExecutableWithDynamicLibC"
                else null
            val heap =
                if (getProperty("heap") != null) "-R:MaxHeapSize=${getProperty("heap")}"
                else null

            listOfNotNull(
                "--enable-http",
                "--enable-url-protocols=classpath",
                "--initialize-at-build-time=com.hexagonkt.core.ClasspathHandler",
                https,
                monitoring,
                mostlyStatic,
                heap,
            )
            .forEach(buildArgs::add)
        }
    }
}
