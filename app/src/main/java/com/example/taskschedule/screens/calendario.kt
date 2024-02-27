package com.example.taskschedule.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.ceil
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Locale


@Composable
fun CalendarTopBar(modifier: Modifier =Modifier, onPreviousMonthClick: () -> Unit, onNextMonthClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonthClick) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Month")
        }

        Text(text = "Enero")

        IconButton(onClick = onNextMonthClick) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}
@Composable
fun DatePickerComposable() {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Column {
        Button(onClick = {
            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                Toast.makeText(context, "Fecha seleccionada: $dayOfMonth/${month + 1}/$year", Toast.LENGTH_SHORT).show()
            }, year, month, day).show()
        }) {
            Text("Seleccionar Fecha")
        }

        // Botón vacío 1
        Button(onClick = {
            // Implementa tu lógica aquí
        }) {
            Text("Botón 1")
        }

        // Botón vacío 2
        Button(onClick = {
            // Implementa tu lógica aquí
        }) {
            Text("Botón 2")
        }
    }
}

@Preview
@Composable
fun previewCalendar(){
    DatePickerComposable()
}
@Preview
@Composable
fun CalendarPreview() {

}