@file:Suppress("UnstableApiUsage")

import com.android.build.api.variant.FilterConfiguration
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization")
    alias(libs.plugins.protobuf)
    alias(libs.plugins.compose.compiler)
}
apply(plugin = "dagger.hilt.android.plugin")

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

val splitApks = !project.hasProperty("noSplits")

val abiFilterList = (properties["ABI_FILTERS"] as String).split(';')


android {
    if (keystorePropertiesFile.exists()) {
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        signingConfigs {
            getByName("debug")
            {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"].toString()
            }
        }
    }

    compileSdk = 34



    defaultConfig {
        applicationId = "com.hubtik.video"
        minSdk = 27
        targetSdk = 34
        versionCode = 25

        if (splitApks) {
            splits {
                abi {
                    isEnable = true
                    reset()
                    include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                    isUniversalApk = true
                }
            }
        }

        versionName = "0.1.2.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ksp {
            arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
            arg("room.incremental", "true")
        }
        if (!splitApks) {
            ndk {
                abiFilters.addAll(abiFilterList)
            }
        }
        resValue("string", "APPLOVIN_SDK_KEY", "\"" + getLocalProperties()?.getProperty("keyApplovin")+ "\"")
        buildConfigField("String", "HOME_NATIVE", "\"" + getLocalProperties()?.getProperty("homeNative") + "\"")
        buildConfigField("String", "HOME_REWARDED", "\"" + getLocalProperties()?.getProperty("homeRewarded") + "\"")
        buildConfigField("String", "DOWNLOAD_LIST", "\"" + getLocalProperties()?.getProperty("downloadList") + "\"")
        buildConfigField("String", "PREMIUM_MONTH", "\"" + getLocalProperties()?.getProperty("monthSub") + "\"")
    }
    val abiCodes = mapOf("armeabi-v7a" to 1, "arm64-v8a" to 2, "x86" to 3, "x86_64" to 4)

    androidComponents {
        onVariants { variant ->

            variant.outputs.forEach { output ->
                val name =
                    if (splitApks) {
                        output.filters.find { it.filterType == FilterConfiguration.FilterType.ABI }?.identifier
                    } else {
                        abiFilterList.firstOrNull()
                    }

                val baseAbiCode = abiCodes[name]

                if (baseAbiCode != null) {
                    output.versionCode.set(baseAbiCode + (output.versionCode.get() ?: 0))
                }

            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

        }
        debug {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("debug")
            }
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "GoPush Debug")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    lint {
        disable.addAll(listOf("MissingTranslation", "ExtraTranslation", "MissingQuantity"))
    }

    applicationVariants.all {
        outputs.all {
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "Download-${defaultConfig.versionName}-${name}.apk"
        }
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs.useLegacyPackaging = true
    }
    androidResources {
        generateLocaleConfig = true
    }

    namespace = "com.junkfood.seal"
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

kotlin {
    jvmToolchain(17)
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {

    implementation(project(":color"))
    implementation(project(":billing"))
    implementation(project(":in-app-update"))

    //Core libs for the app
    implementation(libs.bundles.core)

    //Lifecycle support for Jetpack Compose
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    //Material UI, Accompanist...
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidxCompose)
    implementation(libs.bundles.accompanist)

    //Coil (For Jetpack Compose)
    implementation(libs.coil.kt.compose)

    //Serialization
    implementation(libs.kotlinx.serialization.json)

    //DI (Dependency Injection - Hilt)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.ext.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Database powered by Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Network requests (OkHttp)
    implementation(libs.okhttp)

    //YoutubeDl for Android (youtubedl-android)
    implementation(libs.bundles.youtubedlAndroid)

    //SVG implementation (AndroidSVG by Caverock)
    implementation(libs.caverock.androidsvg)

    //MMKV (Ultrafast Key-Value storage)
    implementation(libs.mmkv)

    //Unit testing libraries
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
//  androidTestImplementation(libs.androidx.compose.ui.test)

    //UI debugging library for Jetpack Compose
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.markdown.android)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.androidx.dataStore.core)
    implementation(libs.exoplayer)
    implementation(libs.play.services.ads.identifier)
    implementation(libs.applovin.sdk)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.billing)
    implementation(libs.billing.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.foundation)
}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        return listOf("room.schemaLocation=${schemaDir.path}")
    }
}

fun getLocalProperties(): Properties? =
    try {
        Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
    } catch (e: Exception) {
        println("Cannot load local.properties $e")
        null
    }
