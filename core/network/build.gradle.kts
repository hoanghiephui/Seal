plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.gradlePlugin)
    id("com.google.devtools.ksp")
}
apply(plugin = "dagger.hilt.android.plugin")
android {
    namespace = "com.android.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.serialization)
    implementation(libs.retrofit.scalars)
    implementation(libs.retrofit.protobuf) {
        exclude( group = "com.google.protobuf", module = "protobuf-java")
    }
    implementation(libs.hilt.android)
    api(project(":core:model"))
    ksp(libs.hilt.ext.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.retrofit.response.keeper)
}
