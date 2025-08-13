plugins {
    id("android-module")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.cyberwarriers.zubzub.feature.splash"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":core:navigation"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 컴포즈 라이브러리
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    // Navigation
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.hilt.navigation.compose)

}