package com.cyberwarriers.zubzub.feature.group_cart.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Effect 발생을 위한 공통 유틸리티
 */
object EffectUtils {
    fun <T> ViewModel.emitEffect(
        effect: T,
        effectFlow: MutableSharedFlow<T>
    ) {
        viewModelScope.launch {
            effectFlow.emit(effect)
        }
    }
} 