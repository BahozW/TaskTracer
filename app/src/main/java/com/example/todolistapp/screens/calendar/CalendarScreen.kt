package com.example.todolistapp.screens.calendar

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolistapp.navigation.Screen.ToDoListScreen
import com.example.todolistapp.ui.components.BackButton
import com.example.todolistapp.utils.Constants.CALENDAR_SCREEN
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    navController: NavController,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CalendarTopBar(navigateBack = navigateBack)
        },
        content = { padding ->
            CalendarContent(
                padding = padding,
                viewModel = viewModel,
                navController = navController ,
                state = state, )
        }
    )
}

@Composable
fun CalendarContent(
    padding: PaddingValues,
    viewModel: CalendarViewModel,
    navController: NavController,
    state: CalendarViewModel.CalendarState
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        MonthYearNavigation(
            viewModel = viewModel,
            state = state
        )

        MonthView(
            yearMonth = state.selectedYearMonth,
            selectedDate = state.selectedDate
        ) { selectedDate ->
            val formattedDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            navController.navigate("${ToDoListScreen.route}/$formattedDate")
        }
    }
}

@Composable
fun MonthYearNavigation(
    viewModel: CalendarViewModel,
    state: CalendarViewModel.CalendarState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { viewModel.previousMonth() }) {
            Icon(Icons.Default.ArrowBackIos, contentDescription = "Previous Month")
        }
        Text(
            text = "${state.selectedYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${state.selectedYearMonth.year}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { viewModel.nextMonth() }) {
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Next Month")
        }
    }
}

@Composable
fun MonthView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInWeek = daysOfWeek.size
    val firstDayOfMonth = yearMonth.atDay(1)

    val dayOfWeekIndex = daysOfWeek.indexOf(firstDayOfMonth.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
    val offset = if (dayOfWeekIndex != -1) dayOfWeekIndex else 6

    val totalDays = yearMonth.lengthOfMonth()
    val totalCells = when {
        (totalDays + offset) % 7 == 0 -> totalDays + offset
        else -> totalDays + offset + (7 - (totalDays + offset) % 7)
    }

    val days = (1..totalCells).map { i ->
        val dayOfMonth = i - offset
        if (dayOfMonth in 1..totalDays) {
            yearMonth.atDay(dayOfMonth)
        } else {
            null
        }
    }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(daysInWeek),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(days) { date ->
                date?.let {
                    DayCell(it, selectedDate, onDateSelected)
                } ?: Spacer(modifier = Modifier.aspectRatio(1f))
            }
        }
    }
}

@Composable
fun DayCell(
    date: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val isToday = date == LocalDate.now()
    val isSelected = date == selectedDate
    val backgroundColor = when {
        isSelected -> Color.Cyan
        isToday -> Color.LightGray
        else -> Color.Transparent
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .background(backgroundColor)
            .padding(4.dp)
            .clickable { onDateSelected(date) }
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun CalendarTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = CALENDAR_SCREEN
            )
        },
        navigationIcon = {
            BackButton(
                navigateBack = navigateBack
            )
        }
    )
}