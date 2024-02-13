plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
    google()
}

group = "org.hse.moodactivities.common"
version = "0.1-DEV"

sourceSets {
    main {
        proto {
            srcDir("utils/src/main/proto")
        }
    }
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.25.1")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
}
