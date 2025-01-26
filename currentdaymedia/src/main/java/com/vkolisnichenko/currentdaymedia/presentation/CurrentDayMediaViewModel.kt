package com.vkolisnichenko.currentdaymedia.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkolisnichenko.core.onFailure
import com.vkolisnichenko.core.onSuccess
import com.vkolisnichenko.currentdaymedia.model.CurrentDayModel
import com.vkolisnichenko.currentdaymedia.usecase.GetCurrentWeekDayUseCase
import com.vkolisnichenko.currentdaymedia.usecase.GetSpecificDayRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentDayMediaViewModel @Inject constructor(
    private val getCurrentDayUseCase: GetCurrentWeekDayUseCase,
    private val getSpecificDayRecordsUseCase: GetSpecificDayRecordsUseCase
) : ViewModel() {
    private val _currentDayMediaState =
        MutableStateFlow<CurrentDayMediaState>(CurrentDayMediaState.Loading)
    val currentDayMediaState: StateFlow<CurrentDayMediaState> = _currentDayMediaState

    fun getCurrentDay(day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCurrentDayUseCase.run(day)
            result.onSuccess {
                _currentDayMediaState.emit(
                    CurrentDayMediaState.Success(CurrentDayModel(currentDayOfTheWeek = it))
                )
            }
        }
    }

    fun fetchCurrentDayRecords(day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userRecords = getSpecificDayRecordsUseCase.run(day)
            userRecords.onSuccess { list ->
                val currentState = _currentDayMediaState.value
                if (currentState is CurrentDayMediaState.Success) {
                    val updatedModel = currentState.currentDayModel.copy(currentDayRecords = list)
                    _currentDayMediaState.emit(CurrentDayMediaState.Success(updatedModel))
                } else {
                    _currentDayMediaState.emit(
                        CurrentDayMediaState.Success(CurrentDayModel(currentDayRecords = list))
                    )
                }
            }
            userRecords.onFailure {

            }
        }
    }

}