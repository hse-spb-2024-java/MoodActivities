plugins {
    id("application")
    id("java")
    id("com.goole.protobuf")
}


group = "org.hse.moodactivities.backend"
version = "0.1-DEV"
grpcVersion = '1.61.1'

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains:annotations:16.0.2")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    compile('org.mongodb.morphia:morphia:2.4.4')
    compile("io.grpc:grpc-netty:${grpcVersion}")
    compile("io.grpc:grpc-protobuf:${grpcVersion}")
    compile("io.grpc:grpc-stub:${grpcVersion}")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "org.hse.moodactivities.backend.HelloWorld"
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

protobuf {
    protoc {
        artifact = 'com.google.protobuf:protoc:3.25.1'
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }
    generateProtoTasks {
        all() * . plugins {
            id("grpc") { }
        }
    }
}