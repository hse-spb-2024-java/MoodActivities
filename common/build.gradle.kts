plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

group = "org.hse.moodactivities.common"
version = "0.1-DEV"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("io.grpc:grpc-netty:1.61.1")
    implementation("io.grpc:grpc-protobuf:1.61.1")
    implementation("io.grpc:grpc-stub:1.61.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

tasks.named("processResources") {
    dependsOn("generateProto")
}

protobuf {
    protoc {
        if (osdetector.os == "osx") {
            artifact = "com.google.protobuf:protoc:3.25.1:osx-x86_64"
        } else {
            artifact = "com.google.protobuf:protoc:3.25.1"
        }
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.61.1"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("build/generated/source/proto/main/grpc")
            srcDir("build/generated/source/proto/main/java")
        }
    }
}


tasks.register<Sync>("SyncProtobufFiles") {
    from("build/generated/source/proto/main/java")
    into("src/main/java/")
    preserve {
        include("/**")
    }
}

tasks.register<Sync>("SyncProtoGrpcDependencies") {
    from("build/generated/source/proto/main/grpc")
    into("src/main/java/")
    preserve {
        include("/**")
    }
}
