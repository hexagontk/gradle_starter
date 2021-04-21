
plugins {
    kotlin("jvm") version "1.4.32"
    id("application")
}

val gradleScripts = properties["gradleScripts"]
val hexagonVersion = properties["hexagonVersion"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")

extensions.configure<JavaApplication> {
    mainClass.set("org.example.GradleStarterKt")
}

dependencies {
    "implementation"("com.hexagonkt:http_server_jetty:$hexagonVersion")

    "testImplementation"("com.hexagonkt:http_client_ahc:$hexagonVersion")
}
