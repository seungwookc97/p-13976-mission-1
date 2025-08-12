plugins {
    kotlin("jvm") version "2.2.0"
}

group = "com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}