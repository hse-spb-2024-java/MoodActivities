plugins {
    java
    application
}

group = "org.hse.moodactivities"
version = "0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:16.0.2")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "org.hse.moodactivities.HelloWorld"
}

tasks.compileJava {
    options.release.set(21)
}

tasks.test {
    useJUnitPlatform()
}
