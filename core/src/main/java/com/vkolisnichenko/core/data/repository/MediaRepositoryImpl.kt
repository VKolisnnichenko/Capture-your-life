package com.vkolisnichenko.core.data.repository

import com.vkolisnichenko.core.domain.model.MediaModel
import com.vkolisnichenko.core.domain.repository.MediaRepository

class MediaRepositoryImpl : MediaRepository {
    private var mediaModel : MediaModel? = null

    override fun saveMedia(mediaModel: MediaModel) {
        this.mediaModel = mediaModel
    }

    override fun getMedia(): MediaModel {
        mediaModel?.let {
            return it
        }
        throw IllegalStateException("Media not found")
    }
}