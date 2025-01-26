package com.vkolisnichenko.mainscreen.domain.model

import android.net.Uri
import com.vkolisnichenko.core.domain.model.MediaModel

data class DayModel(val dayOfWeek: String, val date: String, val media : List<MediaModel>)