
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
//    "implementation"("org.graalvm.sdk:graal-sdk:21.0.0.2")
    "implementation"("com.hexagonkt:http_server_jetty:$hexagonVersion")

    "testImplementation"("com.hexagonkt:http_client_ahc:$hexagonVersion")
}

//tasks.test {
//    jvmArgs(
//        listOf(
//            "-agentlib:native-image-agent=config-output-dir=build/resources/main/META-INF/native-image"
//        )
//    )
//}

/*
$h/Software/graalvm_21_jdk11/bin/native-image \
  --initialize-at-build-time=com.hexagonkt.ClasspathHandler \
  --enable-https \
  -jar build/libs/gradle_starter-all-0.1.0.jar
 */
