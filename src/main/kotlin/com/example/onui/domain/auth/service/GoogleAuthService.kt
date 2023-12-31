package com.example.onui.domain.auth.service

import com.example.onui.domain.auth.presentation.dto.request.OAuthSignInWithAndroidRequest
import com.example.onui.domain.auth.presentation.dto.response.TokenResponse

interface GoogleAuthService {

    fun oauthGoogleSignIn(token: String): TokenResponse

    fun oauthGoogleSignInWith(req: OAuthSignInWithAndroidRequest): TokenResponse
}