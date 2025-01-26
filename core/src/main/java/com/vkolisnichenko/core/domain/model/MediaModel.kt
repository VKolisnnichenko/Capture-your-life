package com.vkolisnichenko.core.domain.model

import android.net.Uri

data class MediaModel(
    val id : Long = -1,
    val mediaUri: Uri,
    val isPhoto: Boolean
)