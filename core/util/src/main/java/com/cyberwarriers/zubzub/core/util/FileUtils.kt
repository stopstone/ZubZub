package com.cyberwarriers.zubzub.core.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

/**
 * 파일 관련 유틸리티 함수들
 * 
 * 주요 기능:
 * - Content URI를 실제 파일로 변환
 * - 이미지 파일 압축 및 최적화
 * - 파일 확장자 및 MIME 타입 처리
 * - 임시 파일 관리
 */
object FileUtils {
    
    private const val TEMP_DIR_NAME = "temp_images"
    private const val MAX_FILE_SIZE = 5 * 1024 * 1024 // 5MB
    private const val PROFILE_IMAGE_SIZE = 240 // 240x240
    
    /**
     * Content URI를 임시 파일로 복사합니다.
     * 
     * @param context Android Context
     * @param contentUri Content URI (content://로 시작하는 URI)
     * @return 복사된 파일의 절대 경로, 실패 시 null
     */
    fun copyContentUriToTempFile(context: Context, contentUri: String): String? {
        return try {
            val uri = Uri.parse(contentUri)
            val contentResolver = context.contentResolver
            
            // 파일 정보 가져오기
            val fileInfo = getFileInfoFromUri(contentResolver, uri)
            val fileName = fileInfo.first ?: "image_${UUID.randomUUID()}"
            val mimeType = fileInfo.second ?: "image/jpeg"
            
            // 파일 확장자 결정
            val extension = getFileExtensionFromMimeType(mimeType) ?: "jpg"
            val finalFileName = if (fileName.contains(".")) fileName else "$fileName.$extension"
            
            // 임시 파일 생성 (압축된 이미지 저장용)
            val tempFile = createTempFile(context, finalFileName)
            
            // 이미지 압축 및 저장
            val compressedSuccess = compressAndSaveImage(context, uri, tempFile)
            if (!compressedSuccess) {
                tempFile.delete()
                logd("이미지 압축 실패")
                return null
            }
            
            logd("Content URI를 임시 파일로 복사 완료: ${tempFile.absolutePath}")
            tempFile.absolutePath
            
        } catch (e: Exception) {
            logd("Content URI 파일 복사 실패: ${e.message}")
            null
        }
    }
    
    /**
     * URI에서 파일 정보를 가져옵니다.
     * 
     * @param contentResolver ContentResolver
     * @param uri URI
     * @return Pair<파일명, MIME타입>
     */
    private fun getFileInfoFromUri(contentResolver: ContentResolver, uri: Uri): Pair<String?, String?> {
        var fileName: String? = null
        var mimeType: String? = null
        
        try {
            // 파일명과 MIME 타입 쿼리
            contentResolver.query(
                uri,
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.MIME_TYPE
                ),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val typeIndex = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                    
                    if (nameIndex >= 0) {
                        fileName = cursor.getString(nameIndex)
                    }
                    if (typeIndex >= 0) {
                        mimeType = cursor.getString(typeIndex)
                    }
                }
            }
            
            // MIME 타입을 ContentResolver에서도 시도
            if (mimeType == null) {
                mimeType = contentResolver.getType(uri)
            }
            
        } catch (e: Exception) {
            logd("URI 정보 가져오기 실패: ${e.message}")
        }
        
        return Pair(fileName, mimeType)
    }
    
    /**
     * MIME 타입에서 파일 확장자를 가져옵니다.
     * 
     * @param mimeType MIME 타입
     * @return 파일 확장자 (점 없이)
     */
    private fun getFileExtensionFromMimeType(mimeType: String): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }
    
    /**
     * 임시 파일을 생성합니다.
     * 
     * @param context Context
     * @param fileName 파일명
     * @return 생성된 임시 파일
     */
    private fun createTempFile(context: Context, fileName: String): File {
        val tempDir = File(context.cacheDir, TEMP_DIR_NAME)
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        
        // 고유한 파일명 생성 (중복 방지)
        val uniqueFileName = "${System.currentTimeMillis()}_$fileName"
        return File(tempDir, uniqueFileName)
    }
    
    /**
     * 임시 파일들을 정리합니다.
     * 
     * @param context Context
     */
    fun cleanupTempFiles(context: Context) {
        try {
            val tempDir = File(context.cacheDir, TEMP_DIR_NAME)
            if (tempDir.exists()) {
                tempDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        val deleted = file.delete()
                        logd("임시 파일 삭제: ${file.name}, 성공: $deleted")
                    }
                }
            }
        } catch (e: Exception) {
            logd("임시 파일 정리 실패: ${e.message}")
        }
    }
    
    /**
     * 특정 파일을 삭제합니다.
     * 
     * @param filePath 삭제할 파일 경로
     * @return 삭제 성공 여부
     */
    fun deleteFile(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            val deleted = file.delete()
            logd("파일 삭제: $filePath, 성공: $deleted")
            deleted
        } catch (e: Exception) {
            logd("파일 삭제 실패: ${e.message}")
            false
        }
    }
    
    /**
     * 파일이 이미지인지 확인합니다.
     * 
     * @param filePath 파일 경로
     * @return 이미지 파일 여부
     */
    fun isImageFile(filePath: String): Boolean {
        val extension = File(filePath).extension.lowercase()
        return extension in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
    }
    
    /**
     * 이미지를 압축하여 저장합니다.
     * 
     * @param context Android Context
     * @param sourceUri 원본 이미지 URI
     * @param targetFile 저장할 파일
     * @return 압축 성공 여부
     */
    private fun compressAndSaveImage(context: Context, sourceUri: Uri, targetFile: File): Boolean {
        return try {
            val contentResolver = context.contentResolver
            
            // 원본 이미지를 Bitmap으로 로드
            val originalBitmap = contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            } ?: return false
            
            // EXIF 정보를 읽어서 회전 각도 확인
            val rotation = getImageRotation(context, sourceUri)
            
            // 이미지 회전 적용
            val rotatedBitmap = if (rotation != 0) {
                rotateBitmap(originalBitmap, rotation)
            } else {
                originalBitmap
            }
            
            // 240x240으로 리사이즈 (정사각형, 크롭)
            val resizedBitmap = resizeImageToSquare(rotatedBitmap, PROFILE_IMAGE_SIZE)
            
            // JPEG로 압축하여 저장 (품질 85%)
            FileOutputStream(targetFile).use { outputStream ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            }
            
            // 메모리 정리
            if (rotatedBitmap != originalBitmap) {
                originalBitmap.recycle()
            }
            resizedBitmap.recycle()
            
            logd("이미지 압축 완료: ${targetFile.length()} bytes")
            true
            
        } catch (e: Exception) {
            logd("이미지 압축 실패: ${e.message}")
            false
        }
    }
    
    /**
     * 이미지의 EXIF 회전 정보를 가져옵니다.
     * 
     * @param context Android Context
     * @param uri 이미지 URI
     * @return 회전 각도
     */
    private fun getImageRotation(context: Context, uri: Uri): Int {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val exif = ExifInterface(inputStream)
                when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }
            } ?: 0
        } catch (e: Exception) {
            logd("EXIF 정보 읽기 실패: ${e.message}")
            0
        }
    }
    
    /**
     * Bitmap을 회전시킵니다.
     * 
     * @param bitmap 원본 Bitmap
     * @param degrees 회전 각도
     * @return 회전된 Bitmap
     */
    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees.toFloat())
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    /**
     * 이미지를 정사각형으로 리사이즈합니다 (중앙 크롭).
     * 
     * @param bitmap 원본 Bitmap
     * @param targetSize 목표 크기
     * @return 리사이즈된 Bitmap
     */
    private fun resizeImageToSquare(bitmap: Bitmap, targetSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // 정사각형 크롭을 위한 크기 계산
        val size = minOf(width, height)
        val x = (width - size) / 2
        val y = (height - size) / 2
        
        // 정사각형으로 크롭
        val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)
        
        // 목표 크기로 스케일
        val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, targetSize, targetSize, true)
        
        // 메모리 정리
        if (croppedBitmap != bitmap) {
            croppedBitmap.recycle()
        }
        
        return scaledBitmap
    }
}
