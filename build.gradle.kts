import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import java.lang.System.getProperty
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
    kotlin("jvm") version("2.1.10")
    id("org.graalvm.buildtools.native") version("0.10.4")
}

val hexagonVersion = "4.0.0-B1"
val gradleScripts = "../hexagon/gradle"

ext.set("modules", "java.logging,java.management")
ext.set("options", "-Xmx48m")
ext.set("applicationClass", "org.example.ApplicationKt")

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")
apply(from = "$gradleScripts/native.gradle")

defaultTasks("build")

version="1.0.0"
group="org.example"
description="Service's description"

dependencies {
    "implementation"("com.hexagontk.http:http_server_helidon:$hexagonVersion")

    "testImplementation"("com.hexagontk.http:http_client_jetty:$hexagonVersion")
    "testImplementation"("org.slf4j:slf4j-nop:2.0.16")
}

tasks.wrapper {
    gradleVersion = "8.12.1"
    distributionType = ALL
}

extensions.configure<GraalVMExtension> {
    fun option(name: String, value: (String) -> String): String? =
        getProperty(name)?.let(value)

    binaries {
        named("main") {
            listOfNotNull(
                option("static") { "--static" },
                option("enableMonitoring") { "--enable-monitoring" },
                option("pgoInstrument") { "--pgo-instrument" },
                option("pgo") { "--pgo=../../../default.iprof" },
            )
            .forEach(buildArgs::add)
        }
        named("test") {
            listOfNotNull(
                option("pgoInstrument") { "--pgo-instrument" },
            )
            .forEach(buildArgs::add)
        }
    }
}
