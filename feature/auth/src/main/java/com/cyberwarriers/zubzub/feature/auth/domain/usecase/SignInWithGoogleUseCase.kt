package com.cyberwarriers.zubzub.feature.auth.domain.usecase

import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(account: GoogleSignInAccount): Result<Unit> {
        return authRepository.signWithGoogle(account)
    }
}