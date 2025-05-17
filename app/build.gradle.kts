plugins {
    id("android-application-module")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.cyberwarriers.zubzub"
}

dependencies {
    // 모듈 의존성
    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:main"))

    // 네비게이션
    implementation(libs.androidx.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
