package com.cyberwarriers.zubzub.core.navigation

import java.net.URLEncoder

object Route {
    // 스플래시
    const val Splash = "splash"

    // 인증
    const val Login = "login"

    // 프로필 생성
    const val ProfileCreate = "profile_create"
    
    // 이미지 선택
    const val ImagePicker = "image_picker?currentImageUri={currentImageUri}"
    
    // 이미지 선택 네비게이션 (현재 이미지 URI 포함)
    fun imagePicker(currentImageUri: String? = null): String {
        return if (currentImageUri != null) {
            "image_picker?currentImageUri=${URLEncoder.encode(currentImageUri, "UTF-8")}"
        } else {
            "image_picker"
        }
    }

    // 메인 화면
    const val Main = "main"

    // 그룹 생성 화면
    const val GroupCreate = "group_create"
    
    // 그룹 생성 확인 화면
    const val CreateConfirm = "create_confirm/{groupId}"
    
    // 그룹 생성 확인 화면 네비게이션
    fun createConfirm(groupId: String) = "create_confirm/$groupId"

    // 그룹 입장 화면
    const val GroupEnter = "group_enter"
    
    // 그룹 확인 화면
    const val GroupConfirm = "group_confirm/{groupId}"
    
    // 그룹 확인 화면 네비게이션
    fun groupConfirm(groupId: String) = "group_confirm/$groupId"

    // 그룹 카트 화면
    const val GroupCart = "group_cart/{groupId}"
    
    // 그룹 카트 화면 네비게이션
    fun groupCart(groupId: String) = "group_cart/$groupId"

    // 바텀 네비게이션
    object BottomNav {
        const val Home = "home"
        const val Profile = "profile"
        const val Third = "third"
    }
}