package com.vkolisnichenko.core.presentation

interface Mapper <IN,OUT> {
    fun map(input: IN): OUT
}