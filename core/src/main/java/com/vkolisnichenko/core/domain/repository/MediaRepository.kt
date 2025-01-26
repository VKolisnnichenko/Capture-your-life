package com.vkolisnichenko.core.domain.repository

import com.vkolisnichenko.core.domain.model.MediaModel

interface MediaRepository {
    fun saveMedia(mediaModel: MediaModel)
    fun getMedia(): MediaModel
}