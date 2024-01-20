import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import java.lang.System.getProperty
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
    kotlin("jvm") version("1.9.22")
    id("org.graalvm.buildtools.native") version("0.9.28")
}

val hexagonVersion = "3.4.6"
val gradleScripts = "https://raw.githubusercontent.com/hexagonkt/hexagon/$hexagonVersion/gradle"

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
    "implementation"("com.hexagonkt:http_server_netty:$hexagonVersion")

    "testImplementation"("com.hexagonkt:http_client_jetty:$hexagonVersion")
    "testImplementation"("org.slf4j:slf4j-nop:2.0.9")
}

tasks.wrapper {
    gradleVersion = "8.5"
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
