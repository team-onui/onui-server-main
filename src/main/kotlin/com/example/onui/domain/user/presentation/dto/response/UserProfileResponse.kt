﻿package com.example.onui.domain.user.presentation.dto.response

data class UserProfileResponse(

    val sub: String,

    val name: String,

    val profileTheme: String,

    val theme: String,

    val onFiltering: Boolean
)
