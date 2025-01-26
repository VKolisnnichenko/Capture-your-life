package com.vkolisnichenko.mainscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkolisnichenko.core.onSuccess
import com.vkolisnichenko.mainscreen.domain.model.DayModel
import com.vkolisnichenko.mainscreen.domain.usecase.FetchAllDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fetchAllDataUseCase: FetchAllDataUseCase,
) : ViewModel() {
    private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState

    fun getMediaDays() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = fetchAllDataUseCase.run(Unit)
            result.onSuccess { response ->
                val mediaDays = mutableListOf<DayModel>()
                response.forEach {
                    mediaDays.add(
                        DayModel(
                            dayOfWeek = it.dayOfWeek,
                            date = it.date,
                            media = it.media
                        )
                    )

                }
                _mainScreenState.emit(MainScreenState.Success(mediaDays))
            }
        }

    }
}