import com.android.build.gradle.LibraryExtension
import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.implementation
import gradle.kotlin.dsl.accessors._8e8a6dd48b2094ffcd3758431423791d.ksp
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

extensions.configure<LibraryExtension> {
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Config.jvmTarget)
        targetCompatibility = JavaVersion.toVersion(Config.jvmTarget)
    }

    kotlinOptions {
        jvmTarget = Config.jvmTarget
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