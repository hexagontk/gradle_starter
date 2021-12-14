
plugins {
    kotlin("jvm") version "1.6.0"
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
    implementation("com.hexagonkt:http_server_jetty:$hexagonVersion")
    implementation("com.hexagonkt:logging_slf4j_jul:$hexagonVersion")

    testImplementation("com.hexagonkt:http_client_ahc:$hexagonVersion")
}

tasks.register<Exec>("nativeImage") {
    dependsOn("jarAll")

    commandLine(
        listOf(
            "${System.getProperty("user.home")}/.sdkman/candidates/java/current/bin/native-image",
            "--no-fallback",
            "--enable-http",
            "--enable-https",
            "--initialize-at-build-time=com.hexagonkt.core.ClasspathHandler",
            "-jar",
            "$buildDir/libs/${project.name}-all-${project.version}.jar",
            "$buildDir/${project.name}",
        )
    )
}

tasks.register<Exec>("upx") {
    dependsOn("nativeImage")

    commandLine(listOf("upx", "$buildDir/${project.name}"))
}

// Should be executed in integration tests (application running in its own VM). To avoid including
// tests classpath into image configuration
// Check https://www.graalvm.org/reference-manual/native-image/BuildConfiguration/#embedding-a-configuration-file
// on how to add Native Image config to libraries' jars (using guid/aid in META-INF)
//
//tasks.test {
//    val dir="$buildDir/resources/main"
//    val filter="$projectDir/src/test/resources/native_image_filter.json"
//    val configOutput="config-output-dir=$dir/META-INF/native-image"
//    val accessFilter="access-filter-file=$filter"
//    val callerFilter="caller-filter-file=$filter"
//    jvmArgs(listOf("-agentlib:native-image-agent=$configOutput,$accessFilter,$callerFilter"))
//}

// native_image_filter.json
//
//{
//  "rules": [
//    { "excludeClasses": "io.netty.**" },
//    { "excludeClasses": "org.asynchttpclient.**" },
//    { "excludeClasses": "org.gradle.**" },
//    { "excludeClasses": "org.jacoco.**" },
//    { "excludeClasses": "org.junit.**" }
//  ]
//}
