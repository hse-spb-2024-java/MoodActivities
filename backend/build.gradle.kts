plugins {
    id("application")
    id("java")
    id("com.google.protobuf") version "0.9.4"
}


group = "org.hse.moodactivities.backend"
version = "0.1-DEV"

dependencies {
    implementation(project(":common"))

    // Env Reader
    implementation("io.github.cdimascio:dotenv-java:2.2.0")

    // JSON
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("org.json:json:20240303")

    // Annotations
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // L4J
    implementation("org.slf4j:slf4j-api:${property("l4jVersion")}")
    implementation("org.slf4j:slf4j-simple:${property("l4jVersion")}")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Mongo
    implementation("dev.morphia.morphia:morphia-core:2.4.11")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")

    // GRPC and Protobuf
    implementation("io.grpc:grpc-netty:${property("grpcVersion")}")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("io.grpc:grpc-protobuf:${property("grpcVersion")}")
    implementation("io.grpc:grpc-stub:${property("grpcVersion")}")
    implementation("io.grpc:grpc-inprocess:1.64.0")
    testImplementation("io.grpc:grpc-core:1.64.0")

    // Hibernate
    implementation(platform("org.hibernate.orm:hibernate-platform:${property("hibernatePlatformVersion")}"))
    implementation("org.hibernate.orm:hibernate-core")

    // JPA
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Postgres
    runtimeOnly("org.postgresql:postgresql:42.6.0")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${property("jjwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jjwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jjwtVersion")}")

    // Bcrypt
    implementation("at.favre.lib:bcrypt:0.10.2")

    // Google
    implementation("com.google.api-client:google-api-client:1.31.5")

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")

    testImplementation("io.grpc:grpc-testing:1.44.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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
