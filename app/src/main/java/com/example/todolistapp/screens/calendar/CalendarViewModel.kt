package com.example.todolistapp.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    private val _state = MutableStateFlow(CalendarState(YearMonth.now(), LocalDate.now()))
    val state: StateFlow<CalendarState> = _state.asStateFlow()

    fun nextMonth() = viewModelScope.launch {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
        updateState()
    }

    fun previousMonth() = viewModelScope.launch {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
        updateState()
    }

    private fun updateState() {
        _state.value = CalendarState(_currentYearMonth.value, _selectedDate.value)
    }

    data class CalendarState(val selectedYearMonth: YearMonth, val selectedDate: LocalDate)
}
