import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")
}

java {
    sourceCompatibility = JavaVersion.toVersion(Config.jvmTarget)
    targetCompatibility = JavaVersion.toVersion(Config.jvmTarget)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = Config.jvmTarget
}
