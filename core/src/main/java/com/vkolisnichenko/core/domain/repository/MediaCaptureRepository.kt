package com.vkolisnichenko.core.domain.repository

import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity

interface MediaCaptureRepository {
    fun insertMediaCapture(memoryCaptureEntity: MemoryCaptureEntity)
    fun updateMediaCapture(mediaId: Long, userNotes : String)
    fun getAllMediaCaptures(): List<MemoryCaptureEntity>
    fun getMediaCaptureById(id: Long): MemoryCaptureEntity
    fun deleteMediaCaptureById(id: Long)
}