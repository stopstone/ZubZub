package com.cyberwarriers.zubzub.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 타임스탬프(초 단위)를 한국어 날짜 형식으로 변환
 * @return "yyyy년 MM월 dd일" 형식의 문자열
 */
fun Long.toKoreanDateString(): String {
    val date = Date(this * 1000) // 초 단위를 밀리초로 변환
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
    return formatter.format(date)
}

/**
 * 타임스탬프(초 단위)를 간단한 날짜 형식으로 변환
 * @return "yyyy.MM.dd" 형식의 문자열
 */
fun Long.toSimpleDateString(): String {
    val date = Date(this * 1000)
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return formatter.format(date)
}

/**
 * 타임스탬프(초 단위)를 상세한 날짜/시간 형식으로 변환
 * @return "yyyy년 MM월 dd일 HH:mm" 형식의 문자열
 */
fun Long.toKoreanDateTimeString(): String {
    val date = Date(this * 1000)
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREAN)
    return formatter.format(date)
}

/**
 * 타임스탬프(밀리초 단위)를 한국어 날짜 형식으로 변환
 * @return "yyyy년 MM월 dd일" 형식의 문자열
 */
fun Long.toKoreanDateStringFromMillis(): String {
    val date = Date(this) // 이미 밀리초 단위
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
    return formatter.format(date)
} 