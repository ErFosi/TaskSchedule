package com.example.taskschedule.screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

@Composable
fun LineaPunteada(){
    val efecto = PathEffect.dashPathEffect(floatArrayOf(10f,10f),phase=0f)
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(color= Color.Gray,
            start = Offset(0f,0f),
            end = Offset(size.width,0f),
            pathEffect = efecto
        )
    }
}