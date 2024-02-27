plugins {
    id("application")
    id("java")
    id("com.google.protobuf") version "0.9.4"
}


group = "org.hse.moodactivities.backend"
version = "0.1-DEV"

dependencies {
    implementation(project(":common"))

    // Annotations
    implementation("org.jetbrains:annotations:16.0.2")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // L4J
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.slf4j:slf4j-simple:2.0.12")

    // Mongo
    implementation("dev.morphia.morphia:morphia-core:2.4.11")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")

    // GRPC and Protobuf
    implementation("io.grpc:grpc-netty:${property("grpcVersion")}")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("io.grpc:grpc-protobuf:${property("grpcVersion")}")
    implementation("io.grpc:grpc-stub:${property("grpcVersion")}")

    // Hibernate
    implementation(platform("org.hibernate.orm:hibernate-platform:${property("hibernatePlatformVersion")}"))
    implementation("org.hibernate.orm:hibernate-core")

    // JPA
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Postgres
    runtimeOnly("org.postgresql:postgresql:42.6.0")

    // Tests
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
