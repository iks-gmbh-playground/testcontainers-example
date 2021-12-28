plugins {
    id("org.springframework.boot") version "2.6.2" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("plugin.spring") version "1.6.10" apply false
    kotlin("plugin.jpa") version "1.6.10" apply false
    kotlin("jvm") version "1.6.10" apply false
    kotlin("plugin.serialization") version "1.6.10" apply false
    id("com.google.cloud.tools.jib") version "3.1.4" apply false
}

tasks.register("install") {
//    dependsOn(":admin-server:installDist")
}

allprojects {
    group = "de.clsky"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    ext.apply {
        set("testcontainers_version", "1.16.2")
        set("junit_version", "5.8.2")
        set("spring_boot_version", "2.6.2")
        set("logback_version", "1.2.3")
        set("coroutines_version", "1.3.5")
        set("serialization_version", "1.0.1")
    }
}