
plugins {
    kotlin("jvm") version("1.7.20")
    id("org.jetbrains.dokka") version("1.7.20")
    id("org.graalvm.buildtools.native") version("0.9.16")
}

val gradleScripts = properties["gradleScripts"]
val hexagonVersion = properties["hexagonVersion"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")
apply(from = "$gradleScripts/native.gradle")

extensions.configure<JavaApplication> {
    mainClass.set("org.example.GradleStarterKt")
}

dependencies {
    "implementation"("com.hexagonkt:http_server_netty:$hexagonVersion")
    "implementation"("com.hexagonkt:logging_slf4j_jul:$hexagonVersion")

    "testImplementation"("com.hexagonkt:http_client_jetty:$hexagonVersion")
}
