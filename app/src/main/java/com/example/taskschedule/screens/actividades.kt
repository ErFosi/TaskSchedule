package com.example.taskschedule.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.taskschedule.ActivitiesViewModel
import com.example.taskschedule.data.Actividad


@Composable
fun actividad(actividad: Actividad, actividadesViewModel: ActivitiesViewModel){
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF0A0F24), Color(0xFF233E8B))
    )
    val backgroundColor = Color(0xFF4F679F)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray)

    ) {
        Column(modifier = Modifier.background(backgroundColor)) {
            // Nombre en una barra superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = gradientBrush)
                    .padding(8.dp),

                ) {
                Text(
                    text = actividad.nombre,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    textAlign = TextAlign.Left
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Tiempo: ${actividad.tiempo.toString()}",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {actividadesViewModel.togglePlay(actividad)}) {
                        Icon(
                            imageVector = if (actividad.isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                            contentDescription = if (actividad.isPlaying) "Stop" else "Play",
                            tint = Color(0xFFC7CBD5)
                        )
                    }
                    // Remove button
                    IconButton(onClick = { actividadesViewModel.onRemoveClick(actividad) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = Color(0xFFC7CBD5)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ListaActividadesUI(actividadesViewModel: ActivitiesViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Actividad")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ListaActividades(actividades = actividadesViewModel.actividades, actividadesViewModel = actividadesViewModel)
        }
    }

    // Diálogo para agregar actividad
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Agregar Actividad") },
            text = {
                // TextField para ingresar el nombre de la nueva actividad
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text("Nombre de la actividad") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        actividadesViewModel.agregarActividad(textState.value)
                        showDialog.value = false
                        textState.value = ""
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ListaActividades(actividades: List<Actividad>, modifier: Modifier = Modifier, actividadesViewModel: ActivitiesViewModel) {
    LazyColumn(modifier = modifier.padding(vertical = 8.dp)) {
        items(items = actividades) { actividad ->
            actividad(actividad,actividadesViewModel)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun actividadPreview(){
    var viewModelo:ActivitiesViewModel= ActivitiesViewModel()
    viewModelo.agregarActividad("TEST DE VIEWMODEL")
    var actividad=Actividad(id=1,nombre = "Test 1", tiempo = 200)
    actividad(actividad, actividadesViewModel = viewModelo)
}

@Preview(showBackground = true)
@Composable
fun listaFuncionalPreview(){
    var viewModelo:ActivitiesViewModel= ActivitiesViewModel()
    viewModelo.agregarActividad("TEST DE VIEWMODEL")
    ListaActividadesUI(actividadesViewModel = viewModelo)
}

@Preview(showBackground = true)
@Composable
fun listaPreview(){
    var viewModelo:ActivitiesViewModel= ActivitiesViewModel()
    viewModelo.agregarActividad("TEST DE VIEWMODEL")
    val actividades = listOf(
        Actividad(id=2,
            nombre = "Leer",
            tiempo = 30*60,
        ),
        Actividad(id=3,
            nombre = "Escribir",
            tiempo = 45,

        ),
        Actividad(id=4,
            nombre = "Quedar con la novia",
            tiempo = 1,

        ),
        Actividad(id=4,
            nombre = "Fumar mata",
            tiempo = 1,

        ),
        Actividad(id=5,
            nombre = "Leer berserk",
            tiempo = 1,

        ),
        Actividad(id=6,
            nombre = "Pablo",
            tiempo = 1,

        ),
        Actividad(id=7,
            nombre = "Escribir",
            tiempo = 1,

        ),

    )
    ListaActividades(actividades = actividades, actividadesViewModel = viewModelo)
}