plugins {
    id("application")
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

group = "org.hse.moodactivities.backend"
version = "0.1-DEV"

dependencies {
    implementation(project(":common"))

    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("io.grpc:grpc-netty:1.61.1")
    implementation("io.grpc:grpc-protobuf:1.61.1")
    implementation("io.grpc:grpc-stub:1.61.1")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains:annotations:16.0.2")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("dev.morphia.morphia:morphia-core:2.4.11")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "org.hse.moodactivities.server.AppServer"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.jar {
    manifest {
        attributes(Pair("Main-Class", application.mainClass))
    }
}


tasks.test {
    useJUnitPlatform()
}
