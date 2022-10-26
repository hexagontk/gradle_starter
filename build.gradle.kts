import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import proguard.gradle.ProGuardTask

plugins {
    kotlin("jvm") version("1.7.20")
    id("org.jetbrains.dokka") version("1.7.20")
    id("org.graalvm.buildtools.native") version("0.9.16")
}

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.2.2")
    }
}

val gradleScripts = properties["gradleScripts"]
val hexagonVersion = properties["hexagonVersion"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")

extensions.configure<JavaApplication> {
    mainClass.set("org.example.GradleStarterKt")
}

// TODO Check: https://www.guardsquare.com/manual/setup/gradle
tasks.register<ProGuardTask>("proguard") {
    dependsOn("jarAll")

    injars(file("build/libs/${project.name}-all-${project.version}.jar"))
    outjars(file("build/libs/${project.name}-proguard-${project.version}.jar"))

    val filter = mapOf("jarfilter" to "!**.jar", "filter" to "!module-info.class")
    libraryjars(filter, "${System.getProperty("java.home")}/jmods/java.base.jmod")
    libraryjars(filter, "${System.getProperty("java.home")}/jmods/java.logging.jmod")
    libraryjars(filter, "${System.getProperty("java.home")}/jmods/java.management.jmod")
    libraryjars(filter, "${System.getProperty("java.home")}/jmods/java.xml.jmod")
    libraryjars(filter, "${System.getProperty("java.home")}/jmods/jdk.unsupported.jmod")

    dontnote("kotlin.**")
    dontnote("kotlinx.**")

    keepkotlinmetadata()

    dontwarn()
    dontoptimize()

    keep("""class org.example.GradleStarterKt {
        |   *;
        |}
    """.trimMargin())
    keep("class com.hexagonkt.**")
    keep("class io.netty.**")
}

dependencies {
    "implementation"("com.hexagonkt:http_server_netty:$hexagonVersion")
    "implementation"("com.hexagonkt:logging_slf4j_jul:$hexagonVersion")

    "testImplementation"("com.hexagonkt:http_client_jetty:$hexagonVersion")
}

graalvmNative {
    metadataRepository {
        enabled.set(true)
    }
}

//extensions.configure<GraalVMReachabilityMetadataRepositoryExtension> {
//    enabled.set(true)
//}

extensions.configure<GraalVMExtension> {
    binaries {
        named("main") {
            listOf(
                "--enable-http",
                "--enable-https",
                "--enable-url-protocols=classpath",
                "--initialize-at-build-time=com.hexagonkt.core.ClasspathHandler",
            )
            .forEach(buildArgs::add)
        }
    }
}

tasks.register<Exec>("upx") {
    dependsOn("nativeCompile")

    val command = "upx $buildDir/native/nativeCompile/${project.name} -o $buildDir/${project.name}"
    commandLine(command.split(" "))
}
