package com.vkolisnichenko.mainscreen.presentation

import com.vkolisnichenko.mainscreen.domain.model.DayModel

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Success(val data: List<DayModel>) : MainScreenState()
    data class Error(val error: String) : MainScreenState()
}