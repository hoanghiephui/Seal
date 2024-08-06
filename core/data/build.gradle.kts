plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}
apply(plugin = "dagger.hilt.android.plugin")
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain(17)
}
android {
    compileSdk = 35
    defaultConfig {
        minSdk = 27
    }
    namespace = "com.android.video.data"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":core:network"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.ext.compiler)
    ksp(libs.hilt.compiler)
}
