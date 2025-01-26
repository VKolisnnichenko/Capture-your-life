package com.vkolisnichenko.mediaconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkolisnichenko.core.onFailure
import com.vkolisnichenko.core.onSuccess
import com.vkolisnichenko.mediaconfirmation.model.ErrorModel
import com.vkolisnichenko.mediaconfirmation.model.MediaConfirmationModel
import com.vkolisnichenko.mediaconfirmation.model.UpdateMediaModel
import com.vkolisnichenko.mediaconfirmation.usecase.GetCaptureMediaUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.GetCurrentMediaUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.InsertMediaCaptureUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.RemoveMediaUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.UpdateMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel

open class MediaConfirmationViewModel @Inject constructor(
    private val getCurrentMediaUseCase: GetCurrentMediaUseCase,
    private val insertMediaCaptureUseCase: InsertMediaCaptureUseCase,
    private val getCaptureByIdMediaUseCase: GetCaptureMediaUseCase,
    private val removeMediaUseCase: RemoveMediaUseCase,
    private val updateMediaUseCase: UpdateMediaUseCase
) : ViewModel() {

    private val _mediaConfirmationScreenState =
        MutableStateFlow<MediaConfirmationState>(MediaConfirmationState.Loading)
    open val mediaConfirmationScreenState: StateFlow<MediaConfirmationState> =
        _mediaConfirmationScreenState

    fun fetchMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val media = getCurrentMediaUseCase.run(Unit)
            media.onSuccess {
                _mediaConfirmationScreenState.value = MediaConfirmationState.Success(
                    MediaConfirmationModel(
                        it.isPhoto,
                        it.mediaUri,
                        getCurrentDate()
                    )
                )
            }

            media.onFailure {
                _mediaConfirmationScreenState.value =
                    MediaConfirmationState.Error(ErrorModel("Media not found", getCurrentDate()))
            }
        }
    }

    fun fetchMediaCaptureById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val media = getCaptureByIdMediaUseCase.run(id)
            media.onSuccess {
                _mediaConfirmationScreenState.value = MediaConfirmationState.Success(it)
            }

            media.onFailure {
                _mediaConfirmationScreenState.value =
                    MediaConfirmationState.Error(ErrorModel("Media not found", getCurrentDate()))
            }
        }
    }

    fun onSaveMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaConfirmationModel =
                (mediaConfirmationScreenState.value as MediaConfirmationState.Success).mediaConfirmationModel
            val result = insertMediaCaptureUseCase.run(mediaConfirmationModel)
            result.onSuccess {
                _mediaConfirmationScreenState.value = MediaConfirmationState.Success(
                    mediaConfirmationModel.copy(onFinishScreen = true)
                )
            }
        }
    }

    fun onUpdateMedia(mediaId : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaConfirmationModel =
                (mediaConfirmationScreenState.value as MediaConfirmationState.Success).mediaConfirmationModel

            val result =
                updateMediaUseCase.run(UpdateMediaModel(mediaId, mediaConfirmationModel.userNotes))
            result.onSuccess {
                _mediaConfirmationScreenState.value = MediaConfirmationState.Success(
                    mediaConfirmationModel.copy(onFinishScreen = true)
                )
            }
        }
    }

    fun onRemoveMedia(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = removeMediaUseCase.run(id)
            result.onSuccess {
                _mediaConfirmationScreenState.value = MediaConfirmationState.Success(
                    MediaConfirmationModel(onFinishScreen = true)
                )
            }
        }
    }


    private fun getCurrentDate() =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

}