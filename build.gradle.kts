import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import java.lang.System.getProperty
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
    kotlin("jvm") version("2.1.10")
    id("org.graalvm.buildtools.native") version("0.10.5")
}

val hexagonVersion = "4.0.1"
val gradleScripts = "https://raw.githubusercontent.com/hexagontk/hexagon/$hexagonVersion/gradle"

ext.set("modules", "jdk.httpserver")
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
    "implementation"("com.hexagontk.http:http_server_jdk:$hexagonVersion")

    "testImplementation"("com.hexagontk.http:http_client_jdk:$hexagonVersion")
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
            // ./gradlew -D=<option> nativeCompile
            listOfNotNull(
                option("static") { "--static" },
                option("mostlyStatic") { "-H:+StaticExecutableWithDynamicLibC" },
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
