import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdkVersion(Config.compileSdk)

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Config.jvmTarget)
        targetCompatibility = JavaVersion.toVersion(Config.jvmTarget)
    }

    buildFeatures {
        compose = true
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = Config.jvmTarget
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Hilt
    implementation(libs.getLibrary("hilt.android"))
    ksp(libs.getLibrary("hilt.compiler"))

    // Coroutines
    implementation(libs.getLibrary("coroutines.android"))
}