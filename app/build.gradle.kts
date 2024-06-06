plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf") version "0.9.4"
}

group = "org.hse.moodactivities.app"
version = "0.1-DEV"

dependencies {
    implementation(project(":common"))

    // grpc
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("io.grpc:grpc-netty:1.61.1")
    implementation("io.grpc:grpc-protobuf:1.61.1")
    implementation("io.grpc:grpc-stub:1.61.1")
    implementation("com.google.protobuf:protobuf-java:3.25.1")

    // annotations
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains:annotations:16.0.2")

    // charts library
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // location dependencies
    implementation("com.github.BirjuVachhani:locus-android:3.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // android dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // jwt
    api("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.12.5") {
        exclude(group = "org.json", module = "json")
    }

    implementation("com.google.android.gms:play-services-auth:20.0.0")

    // location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // tests
    testImplementation("junit:junit:4.13.2")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

android {
    namespace = "org.hse.moodactivities"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.hse.moodactivities"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        pickFirst("META-INF/INDEX.LIST")
        pickFirst("META-INF/io.netty.versions.properties")
    }
}
