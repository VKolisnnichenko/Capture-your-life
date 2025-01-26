package com.vkolisnichenko.core.data.repository

import com.vkolisnichenko.core.data.database.dao.MemoryCaptureDao
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import javax.inject.Inject

class MediaCaptureRepositoryImpl @Inject constructor(
    private val memoryCaptureDao: MemoryCaptureDao
) : MediaCaptureRepository {

    override fun insertMediaCapture(memoryCaptureEntity: MemoryCaptureEntity) {
        memoryCaptureDao.insertMemoryCapture(memoryCaptureEntity)
    }

    override fun updateMediaCapture(mediaId: Long, userNotes : String) {
        memoryCaptureDao.updateMemoryCaptureById(mediaId, userNotes)
    }

    override fun getAllMediaCaptures(): List<MemoryCaptureEntity> {
        return memoryCaptureDao.getAllMemoryCaptures()
    }

    override fun getMediaCaptureById(id: Long): MemoryCaptureEntity {
        return memoryCaptureDao.getMemoryCaptureById(id)
    }

    override fun deleteMediaCaptureById(id: Long) {
        memoryCaptureDao.deleteMemoryCaptureById(id)
    }
}
