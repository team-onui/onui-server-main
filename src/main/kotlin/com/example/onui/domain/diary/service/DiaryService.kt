﻿package com.example.onui.domain.diary.service

import com.example.onui.domain.diary.presentation.request.ChattingWithGPTRequest
import com.example.onui.domain.diary.presentation.request.CreateDiaryRequest
import com.example.onui.domain.diary.presentation.request.UpdateDiaryRequest
import com.example.onui.domain.diary.presentation.response.ChattingResponse
import com.example.onui.domain.diary.presentation.response.DiaryDetailResponse
import com.example.onui.domain.diary.presentation.response.DiaryListResponse
import java.time.LocalDate

interface DiaryService {

    fun createDiary(req: CreateDiaryRequest): DiaryDetailResponse

    fun getDiaryByMonth(year: Int, month: Int): DiaryListResponse

    fun getDetailById(date: LocalDate): DiaryDetailResponse?

    fun update(req: UpdateDiaryRequest): DiaryDetailResponse

    fun getSevenDaysAgo(): DiaryListResponse

    fun chattingWithGPT(req: ChattingWithGPTRequest): ChattingResponse
}
