
plugins {
    kotlin("jvm") version "1.3.72"
    application
}

apply(from = "${properties["gradleScripts"]}/kotlin.gradle")
apply(from = "${properties["gradleScripts"]}/application.gradle")

application {
    mainClassName = "org.example.GradleStarterKt"
}

dependencies {
    implementation("com.hexagonkt:http_server_jetty:${properties["hexagonVersion"]}")

    testImplementation("com.hexagonkt:http_client_ahc:${properties["hexagonVersion"]}")
}
