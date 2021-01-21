
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
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${V.kotlin}") // Required for Kotlin integration
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}