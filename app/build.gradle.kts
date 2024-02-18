plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

group = "org.hse.moodactivities.app"
version = "0.1-DEV"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.hse.moodactivities.common:common")

    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("io.grpc:grpc-netty:1.61.1")
    implementation("io.grpc:grpc-protobuf:1.61.1")
    implementation("io.grpc:grpc-stub:1.61.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains:annotations:16.0.2")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
}
