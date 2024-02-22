val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

project.setProperty("mainClassName", "com.fitnflow.application.ApplicationKt")

plugins {
    application
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"

}

group = "fitnflow"
version = "0.0.1"
application {
    mainClass.set("com.fitnflow.application.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-mustache:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    implementation("io.ktor:ktor-client-logging:$ktor_version")

    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")

    implementation ("io.ktor:ktor-client-gson:$ktor_version")

}