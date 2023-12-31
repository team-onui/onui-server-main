﻿package com.example.onui.domain.timeline.service

import com.example.onui.domain.diary.entity.Diary
import com.example.onui.domain.diary.exception.DiaryNotFoundException
import com.example.onui.domain.diary.repository.DiaryRepository
import com.example.onui.domain.timeline.entity.Comment
import com.example.onui.domain.timeline.exception.AlreadyPostedTimelineException
import com.example.onui.domain.timeline.exception.TimelineNotFoundException
import com.example.onui.domain.timeline.presentation.dto.response.CommentListResponse
import com.example.onui.domain.timeline.presentation.dto.response.CommentResponse
import com.example.onui.domain.timeline.presentation.dto.response.TimelineResponse
import com.example.onui.domain.timeline.repository.CommentRepository
import com.example.onui.domain.timeline.repository.QTimelineRepository
import com.example.onui.global.common.facade.UserFacade
import com.example.onui.global.config.error.exception.PermissionDeniedException
import com.vane.badwordfiltering.BadWordFiltering
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
@Transactional(readOnly = true)
class TimelineServiceImpl(
    private val userFacade: UserFacade,
    private val qTimelineRepository: QTimelineRepository,
    private val diaryRepository: DiaryRepository,
    private val commentRepository: CommentRepository,
    private val badWordFiltering: BadWordFiltering
) : TimelineService {

    @Transactional
    override fun post(id: UUID): TimelineResponse {
        val user = userFacade.getCurrentUser()

        val diary = diaryRepository.findByIdOrNull(id) ?: throw DiaryNotFoundException

        if (diary.user != user) throw PermissionDeniedException

        if (diaryRepository.existsByIdAndIsPosted(id, true)) throw AlreadyPostedTimelineException

        return diaryRepository.save(
            Diary(
                diary.user, diary.content, diary.mood, diary.tagList, diary.createdAt, diary.image, diary.id, true
            )
        ).toTimelineResponse()
    }

    override fun searchByDate(idx: Int, size: Int, date: LocalDate): Page<TimelineResponse> = qTimelineRepository
        .findPageByDate(
            PageRequest.of(idx, size, Sort.by("diary.createdAt").descending()), date, userFacade.getCurrentUser()
        )

    @Transactional
    override fun comment(timelineId: UUID, comment: String): CommentResponse {

        val user = userFacade.getCurrentUser()

        val timeline = diaryRepository.findByIdAndIsPosted(timelineId, true) ?: throw TimelineNotFoundException

        val commented = commentRepository.save(Comment(comment, userFacade.getCurrentUser(), timeline))

        return if (user.onFiltering) commented.toFilteringResponse(badWordFiltering) else commented.toResponse()
    }

    override fun getComment(timelineId: UUID): CommentListResponse {

        val user = userFacade.getCurrentUser()

        val timeline = diaryRepository.findByIdAndIsPosted(timelineId, true) ?: throw TimelineNotFoundException


        val commentList = commentRepository.findAllByTimelineOrderByCreatedAtAsc(timeline).map {
            if (user.onFiltering) it.toFilteringResponse(badWordFiltering)
            else it.toResponse()
        }.toMutableList()

        return CommentListResponse(if (commentList.isEmpty()) null else commentList)
    }


}