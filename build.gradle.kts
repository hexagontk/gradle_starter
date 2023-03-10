
plugins {
    kotlin("jvm") version("1.8.10")
}

val hexagonVersion = "2.6.3"
val gradleScripts = "https://raw.githubusercontent.com/hexagonkt/hexagon/$hexagonVersion/gradle"

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")

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
