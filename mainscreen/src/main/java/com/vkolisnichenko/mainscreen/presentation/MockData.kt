package com.vkolisnichenko.mainscreen.presentation

import com.vkolisnichenko.mainscreen.R

data class MediaDay(
    val dayOfWeek: String,
    val date: String,
    val mediaList: List<Int>
)

val sampleMediaDays = listOf(
    MediaDay("Monday", "October 16, 2024", listOf(R.drawable.seamock, R.drawable.hikemock)),
    MediaDay("Tuesday", "October 17, 2024", listOf(R.drawable.hikemock, R.drawable.seamock)),
    MediaDay("Wednesday", "October 18, 2024", listOf(R.drawable.seamock, R.drawable.hikemock)),
    MediaDay("Monday", "October 16, 2024", listOf(R.drawable.seamock, R.drawable.hikemock)),
    MediaDay("Tuesday", "October 17, 2024", listOf(R.drawable.hikemock, R.drawable.seamock)),
    MediaDay("Monday", "October 16, 2024", listOf(R.drawable.seamock, R.drawable.hikemock)),
    MediaDay("Tuesday", "October 17, 2024", listOf(R.drawable.hikemock, R.drawable.seamock)),
    MediaDay("Monday", "October 16, 2024", listOf(R.drawable.seamock, R.drawable.hikemock)),
    MediaDay("Tuesday", "October 17, 2024", listOf(R.drawable.hikemock, R.drawable.seamock))
)