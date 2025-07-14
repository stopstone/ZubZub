package com.cyberwarriers.zubzub.feature.group_enter.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Effect 발생을 위한 공통 유틸리티
 */
object EffectUtils {
    
    /**
     * Effect를 발생시키는 확장 함수
     * 
     * @param effect 발생시킬 Effect
     * @param effectFlow Effect를 전송할 SharedFlow
     */
    fun <T> ViewModel.emitEffect(
        effect: T,
        effectFlow: MutableSharedFlow<T>
    ) {
        viewModelScope.launch {
            effectFlow.emit(effect)
        }
    }
} 