
plugins {
    kotlin("jvm") version "1.4.20"
    id("application")
}

val gradleScripts = properties["gradleScripts"]
val hexagonVersion = properties["hexagonVersion"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")

application {
    mainClass.set("org.example.GradleStarterKt")
}

dependencies {
    "implementation"("com.hexagonkt:http_server_jetty:$hexagonVersion")

    // Logging
    "implementation"("ch.qos.logback:logback-classic:1.2.3")
//    "implementation"("org.slf4j:slf4j-simple:1.7.30")
//    "implementation"("org.slf4j:slf4j-jdk14:1.7.30")
    "runtimeOnly"("org.slf4j:jcl-over-slf4j:1.7.30")
    "runtimeOnly"("org.slf4j:log4j-over-slf4j:1.7.30")
    "runtimeOnly"("org.slf4j:jul-to-slf4j:1.7.30")

    "testImplementation"("com.hexagonkt:http_client_ahc:$hexagonVersion")
}
