package com.cyberwarriers.zubzub.feature.group_create.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cyberwarriers.zubzub.core.util.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 그룹 생성 확인 화면의 ViewModel
 */
@HiltViewModel
class CreateConfirmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _groupId = MutableStateFlow("")
    val groupId: StateFlow<String> = _groupId.asStateFlow()

    init {
        // SavedStateHandle에서 groupId 파라미터 받기
        val receivedGroupId = savedStateHandle.get<String>("groupId") ?: ""
        _groupId.value = receivedGroupId
        logd("CreateConfirmViewModel - groupId: $receivedGroupId")
    }
} 