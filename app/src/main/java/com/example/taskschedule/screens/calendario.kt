package com.example.taskschedule.screens

import android.app.DatePickerDialog
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.MaterialTheme.colors
import com.example.taskschedule.data.Actividad
import com.example.taskschedule.viewmodels.CalendarViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.xaxis.XAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.YAxisDrawer
import com.github.tehras.charts.piechart.AxisLine
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.text.SimpleDateFormat
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Locale
import kotlin.random.Random


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
fun DatePickerComposable(calendarViewModel: CalendarViewModel) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val fechaSelec by calendarViewModel.fechaSelec.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val lista by calendarViewModel.actividadesFecha.collectAsState(initial = emptyList())
    var mostrarGraficoCircular by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var agrupacionCategoria by remember { mutableStateOf(false) }
    val colorSeleccionado = MaterialTheme.colors.primary
    val colorNoSeleccionado = MaterialTheme.colors.secondaryVariant // Color más tenue cuando no está seleccionado
    Column(modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Row(
            modifier = Modifier
                .padding(10.dp), // Añade un poco de padding para un mejor diseño
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Espacia los elementos uniformemente
        ) {
            // Botón para decrementar la fecha
            IconButton(onClick = { val nuevaFecha = fechaSelec.minusDays(1)
                calendarViewModel.cambioFecha(nuevaFecha)
                Toast.makeText(context, "Fecha seleccionada: ${nuevaFecha.format(formatter)}", Toast.LENGTH_SHORT).show()}) {
                Icon(Icons.Default.ArrowLeft, contentDescription = "Decrementar día")
            }

            // Selector de fecha
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                // Texto y botón para seleccionar fecha
                TextButton(onClick = {
                    DatePickerDialog(context, { _, year, month, dayOfMonth ->
                        val fechaNueva = LocalDate.of(year, month + 1, dayOfMonth)
                        calendarViewModel.cambioFecha(fechaNueva)
                        Toast.makeText(context, "Fecha seleccionada: ${fechaNueva.format(formatter)}", Toast.LENGTH_SHORT).show()
                    }, fechaSelec.year, fechaSelec.monthValue - 1, fechaSelec.dayOfMonth).show()
                }) {
                    Text("Fecha seleccionada: "+"${fechaSelec.format(formatter)}",textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    //Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar fecha")
                }
            }

            // Botón para incrementar la fecha
            IconButton(onClick = { val fechaNueva = fechaSelec.plusDays(1)
                calendarViewModel.cambioFecha(fechaNueva)
                Toast.makeText(context, "Fecha seleccionada: ${fechaNueva.format(formatter)}", Toast.LENGTH_SHORT).show()}) {
                Icon(Icons.Default.ArrowRight, contentDescription = "Incrementar día")
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Graficas", textAlign = TextAlign.Center)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón para el gráfico circular
                IconButton(onClick = { mostrarGraficoCircular = true }) {
                    Icon(
                        Icons.Default.PieChart,
                        contentDescription = "Gráfico Circular",
                        tint = if (mostrarGraficoCircular) colorSeleccionado else colorNoSeleccionado
                    )
                }

                // Espacio entre iconos
                Spacer(modifier = Modifier.width(16.dp))

                // Botón para el gráfico de barras
                IconButton(onClick = { mostrarGraficoCircular = false }) {
                    Icon(
                        Icons.Default.BarChart,
                        contentDescription = "Gráfico de Barras",
                        tint = if (!mostrarGraficoCircular) colorSeleccionado else colorNoSeleccionado
                    )
                }
                Spacer(Modifier.width(8.dp)) // Espacio entre los iconos y el selector de agrupación

                // Texto antes del DropdownMenu
                Text(text = "Agrupado por:")

                // DropdownMenu para seleccionar la agrupación
                var textoElecc = ""
                if (agrupacionCategoria) {
                    textoElecc = "Categoria"
                } else {
                    textoElecc = "Actividad"
                }
                // DropdownMenu para seleccionar la agrupación
                Box {
                    Row( modifier = Modifier
                        .padding(1.dp).clickable(onClick = { expanded = true }), // Añade un poco de padding para un mejor diseño
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween // Espacia los elementos uniformemente
                    ){
                        Text(
                            text = textoElecc,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Selector de gráficos",
                            tint = if (mostrarGraficoCircular) colorSeleccionado else colorNoSeleccionado
                        )

                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem( text = { Text("Actividad") },onClick = {
                            agrupacionCategoria = false
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("Categoria") },onClick = {
                            agrupacionCategoria = true
                            expanded = false
                        })
                    }
                }
            }
        }
        if(mostrarGraficoCircular){
            Tarta(agruparPorCategoria(lista,agrupacionCategoria))
        }
        else{
            Barras(agruparPorCategoria(lista,agrupacionCategoria))
        }

        Spacer(modifier = Modifier.height(35.dp))

        // Botón para "Descargar actividades"
        Button(
            onClick = { /* TODO: Acción de descarga */ },
            // Ajusta el shape para hacerlo más rectangular
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .height(48.dp) // Asegura que el botón sea más alto y rectangular
                .fillMaxWidth(0.8f) // Usa un porcentaje del ancho máximo para tamaño del botón
        ) {
            Text("Descargar actividades")
        }

        // Espacio entre botones
        Spacer(modifier = Modifier.height(35.dp))

        // Botón para "Compartir"
        Button(
            onClick = { /* TODO: Acción de compartir */ },
            // Ajusta el shape para hacerlo más rectangular
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .height(48.dp) // Asegura que el botón sea más alto y rectangular
                .fillMaxWidth(0.8f) // Usa un porcentaje del ancho máximo para tamaño del botón
        ) {
            // Coloca el texto y el icono dentro de un Row para asegurar su correcta disposición
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Compartir")
                // Espacio entre el texto y el icono
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Share, contentDescription = "Compartir")
            }
        }




    }




}



//Esta funcion devuelve una lista de actividades que cuando agrupacionCategoria esta activo devuelve una actividad por categoria con la suma
//de los tiempos de cada categoria, si no la deja como está
fun agruparPorCategoria(lista: List<Actividad>, agrupacionCategoria: Boolean): List<Actividad> {
    // Verifica si la agrupación por categoría está activada
    return if (agrupacionCategoria) {
        // Agrupa las actividades por categoría
        lista.groupBy { it.categoria }
            // Transforma cada grupo en un nuevo objeto Actividad
            .map { entry ->
                Actividad(
                    nombre = entry.key, // Usa el nombre de la categoría
                    tiempo = entry.value.sumOf { it.tiempo }, // Suma los tiempos de todas las actividades en la categoría
                    id = 1 // Puedes ajustar esto según tu modelo de datos
                )
            }
    } else {
        // Si no se requiere agrupación, devuelve la lista original
        lista
    }
}

@Composable
fun Barras(lista: List<Actividad>) {
    var barras=ArrayList<BarChartData.Bar>()
    when {
        lista.isEmpty() -> {
            Text(text = "No hay datos")
        }

        else -> {
            val lista_ord=lista.sortedBy{ it.tiempo }.asReversed()
            var col:Color
            var tiempoSobrante=0
            lista_ord.mapIndexed { index, act ->
                if(index>colores.size-1){
                    tiempoSobrante=tiempoSobrante+act.tiempo
                }
                else{
                    col=colores[index]
                    barras.add(BarChartData.Bar(label = "", value = act.tiempo.toFloat(), color = col))
                }

            }
            if (tiempoSobrante>0){
                barras.add(BarChartData.Bar(label = "", value = tiempoSobrante.toFloat(), color = Color.Gray))
            }


            BarChart(barChartData = BarChartData(bars = barras),modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(220.dp),  yAxisDrawer = SimpleYAxisDrawer(labelValueFormatter = ::formatTimeFloat, axisLineThickness = 0.dp  ), labelDrawer = SimpleValueDrawer(drawLocation = SimpleValueDrawer.DrawLocation.Outside)
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el gráfico y la leyenda

            LeyendaMatriz(lista = lista)
        }
    }
}

@Composable
fun Tarta(lista : List<Actividad>) {
    var slices = ArrayList<PieChartData.Slice>()
    when {
        lista.isEmpty() -> {
            Text(text = "No hay datos")
        }

        else -> {
            lista.mapIndexed { index, act ->
                var col : Color
                if(index>colores.size-1){
                    col=Color.Gray
                }
                else{
                    col=colores[index]
                }
                slices.add(
                    PieChartData.Slice(
                        value = act.tiempo.toFloat(),
                        color = col
                    )
                )
            }

            PieChart(
                pieChartData = PieChartData(slices),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
                    .height(220.dp),
                sliceDrawer = SimpleSliceDrawer(sliceThickness = 100f)

            )
            Log.d("p","Tarta creada:"+slices.size)
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el gráfico y la leyenda

            LeyendaMatriz(lista = lista)
        }
    }
}
var colores = mutableListOf(
    Color(0xFF4CAF50), // Verde
    Color(0xFFFFC107), // Ámbar
    Color(0xFFE91E63), // Rosa
    Color(0xFF9C27B0), // Púrpura
    Color(0xFFFF5722), // Naranja oscuro
    Color(0xFF03A9F4), // Azul claro
    Color(0xFF009688), // Verde azulado
    Color(0xFFFFEB3B), // Amarillo
    //Color(0xFF673AB7), // Púrpura oscuro
    //Color(0xFF8BC34A), // Verde claro
    //Color(0xFFCDDC39), // Lima
    //Color(0xFFFF9800), // Naranja
    //Color(0xFF00BCD4), // Cian
    //Color(0xFF2196F3), // Azul
    //Color(0xFF3F51B5), // Índigo
    //Color(0xFF795548), // Marrón
    //Color(0xFF9E9E9E), // Gris
    Color(0xFF607D8B)  // Azul grisáceo
)

@Composable
fun LeyendaItem(nombre: String, color: Color) {
    var expanded by remember { mutableStateOf(false) }

    // Wrapper para detectar clics y mostrar el DropdownMenu
    Box(modifier = Modifier.clickable { expanded = true }) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .width(100.dp)
            .padding(start = 10.dp)) {
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .background(color = color, shape = RoundedCornerShape(3.dp)) // Usa el color y forma deseados
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = nombre,
                modifier = Modifier
                    .padding(end = 1.dp)
                    .widthIn(max = 60.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier

        ) {
            Text(
                text = nombre,
                modifier = Modifier
                    .padding(4.dp)
                    .heightIn(max = 32.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LeyendaMatriz(lista: List<Actividad>) {

    val numRows = (lista.size + 3) / 4

    Column(Modifier.height(60.dp)) {
        var exit=false
        for (row in 0 until numRows) {
            if (exit){
                break
            }

            Row {
                for (column in 0 until 4) {
                    if (exit){
                        break
                    }
                    val index = row * 4 + column
                    if (index < lista.size) { // Comprobamos para no salirnos de la lista
                        val act = lista[index]
                        val col: Color = if (index >= colores.size) {
                            LeyendaItem(nombre = "Otros", color = Color.Gray)
                            exit=true
                            break
                        } else {
                            colores[index]
                        }
                        LeyendaItem(nombre = act.nombre, color = col)
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun previewCalendar(){
    //DatePickerComposable()
}
@Preview
@Composable
fun CalendarPreview() {

}