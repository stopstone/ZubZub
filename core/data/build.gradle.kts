plugins {
    id("android-module")
}

android {
    namespace = "com.cyberwarriers.zubzub.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:util"))
    
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
}