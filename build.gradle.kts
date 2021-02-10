
plugins {
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.72"
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("application")
    id("java")
}

group = "org.nmccarra1"

application {
     mainClassName = "org.nmccarra1.my.paypal.stats.api.AppKt"
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

object V {
    const val kotlin  = "1.3.72"
    const val fuel = "2.2.0"
    const val config = "1.4.1"
    const val jackson = "2.10.4"
    const val kotlinLogging = "1.7.8"
    const val mockk = "1.9.3"
    const val mockServer = "3.10.8"
}

dependencies {
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${V.kotlin}") // Required for Kotlin integration
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.github.kittinunf.fuel:fuel:${V.fuel}")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:${V.fuel}")
    implementation("com.typesafe:config:${V.config}")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:${V.jackson}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${V.jackson}")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mock-server:mockserver-netty:${V.mockServer}")
    testImplementation("org.mock-server:mockserver-client-java:${V.mockServer}")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}