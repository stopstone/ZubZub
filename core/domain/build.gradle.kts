plugins {
    id("kotlin-module")
}

dependencies {
    // Coroutines (for Flow)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    
    // Javax Inject (for @Inject)
    implementation("javax.inject:javax.inject:1")
}