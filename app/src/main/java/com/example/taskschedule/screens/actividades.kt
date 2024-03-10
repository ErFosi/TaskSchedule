package com.example.taskschedule.screens
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.taskschedule.viewmodels.ActivitiesViewModel
import com.example.taskschedule.data.Actividad
import kotlinx.coroutines.delay
import androidx.compose.ui.res.stringResource
import com.example.taskschedule.R

@Composable
fun AnimatedStripe() {
    val color= MaterialTheme.colorScheme.primary
    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)) {
        val stripeWidth = size.width * 0.2f  // Ancho de la franja
        val animatedOffset = animatedProgress * (size.width + stripeWidth) - stripeWidth

        drawRect(
            color = color,
            topLeft = Offset(x = animatedOffset, y = 0f),
            size = Size(width = stripeWidth, height = size.height)
        )
    }
}
@Composable
fun actividad(actividad: Actividad, actividadesViewModel: ActivitiesViewModel) {
    // Utiliza los colores del tema actual
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
    )
    val backgroundColor = MaterialTheme.colorScheme.background
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant
    val isVisible = remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    val categoriasMap = mapOf(
        stringResource(id = R.string.otros) to "otros",
        stringResource(id = R.string.ocio) to "ocio",
        stringResource(id = R.string.ocupacion) to "ocupacion",
        stringResource(id = R.string.deporte) to "deporte",
        stringResource(id = R.string.diario) to "diario"
    )
    val categoriasMapInverso = mapOf(
        "otros" to stringResource(id = R.string.otros)  ,
        "ocio" to stringResource(id = R.string.ocio) ,
        "ocupacion" to  stringResource(id = R.string.ocupacion),
        "deporte" to stringResource(id = R.string.deporte)  ,
        "diario" to stringResource(id = R.string.diario)
    )
    val categorias = listOf(stringResource(id = R.string.otros), stringResource(id = R.string.ocio), stringResource(id = R.string.ocupacion), stringResource(id = R.string.deporte), stringResource(id = R.string.diario))

    LaunchedEffect(isVisible.value) {
        if (!isVisible.value) {
            // Espera un poco antes de ejecutar la acción de eliminación
            delay(350)
            isVisible.value = true
            actividadesViewModel.onRemoveClick(actividad.id)
        }
    }

    AnimatedVisibility(
        visible = isVisible.value,
        enter = EnterTransition.None,
        exit = fadeOut(animationSpec = tween(durationMillis = 300)) + shrinkVertically(
            animationSpec = tween(
                durationMillis = 300
            )
        ),
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(modifier = Modifier.background(backgroundColor)) {
                // Nombre en una barra superior
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush = gradientBrush)
                        .padding(8.dp)
                ) {
                    Text(
                        text = actividad.nombre,
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
                        textAlign = TextAlign.Left
                    )
                }

                // Contenido de la actividad
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
                            text = stringResource(id = R.string.tiempo)+": ${formatTime(actividad.tiempo)}",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text(stringResource(id = R.string.categoria)+": "+(categoriasMapInverso[actividad.categoria] ?: stringResource(
                                    id = R.string.pulsa
                                )))
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                categoriasMap.forEach { (categoriaLocalizada, valorEnEspanol) ->
                                    DropdownMenuItem(
                                        text = { Text(categoriaLocalizada) },
                                        onClick = {
                                            Log.d("E", valorEnEspanol)
                                            val actCopy = actividad.copy()
                                            actCopy.categoria = valorEnEspanol
                                            actividadesViewModel.updateCategoria(actCopy, valorEnEspanol)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        val contex= LocalContext.current
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { actividadesViewModel.togglePlay(actividad,
                                contex) }) {
                                Icon(
                                    imageVector = if (actividad.isPlaying) Icons.Filled.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (actividad.isPlaying) stringResource(id = R.string.stop) else stringResource(id = R.string.play) ,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = { isVisible.value = false }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(id = R.string.remove),
                                    tint = iconTint
                                )
                            }
                        }
                }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    ) {

                        if (actividad.isPlaying) {
                            AnimatedStripe()
                        }
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
                Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.agregar_act))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ListaActividades(actividadesViewModel = actividadesViewModel)
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(stringResource(id = R.string.agregar_act)) },
            text = {
                // TextField para ingresar el nombre de la nueva actividad
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text(stringResource(id = R.string.nombre)) }
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
                    Text(stringResource(id = R.string.agregar))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text(stringResource(id = R.string.cancelar))
                }
            }
        )
    }
}

@Composable
fun ListaActividades(modifier: Modifier = Modifier, actividadesViewModel: ActivitiesViewModel) {
    val lista = actividadesViewModel.actividades.collectAsState(initial = emptyList()).value
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Pantalla horizontal: usar LazyVerticalGrid para dos columnas
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(vertical = 8.dp)
        ) {
            items(items = lista, key = { actividad ->
                actividad.id
            }) { act ->
                actividad(act, actividadesViewModel)
            }
        }
    } else {
        // Pantalla vertical: usar LazyColumn para una columna
        LazyColumn(modifier = modifier.padding(vertical = 8.dp)) {
            items(items = lista, key = { actividad ->
                actividad.id
            }) { act ->
                actividad(act, actividadesViewModel)
            }
        }
    }
}



/*
@Preview(showBackground = true)
@Composable
fun actividadPreview(){
    var viewModelo by ActivitiesViewModel
    viewModelo.agregarActividad("TEST DE VIEWMODEL")
    var actividad=Actividad(id=1,nombre = "Test 1", tiempo = 200)
    actividad(actividad, actividadesViewModel = viewModelo)
}

@Preview(showBackground = true)
@Composable
fun listaFuncionalPreview(){
    var viewModelo:ActivitiesViewModel= ActivitiesViewModel()
    viewModelo.agregarActividad("TEST DE VIEWMODEL")
    var act=Actividad(122,"prueba Minutos",120)
    var act2=Actividad(123,"prueba Horas",370000000)
    viewModelo.agregarActTest(act)
    viewModelo.agregarActTest(act2)

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
    ListaActividades(actividadesViewModel = viewModelo)
}
*/

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m ${remainingSeconds}s"
        else -> "${remainingSeconds}s"
    }
}

fun formatTimeFloat(seconds: Float): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    if (hours>24){
        return(">24h")
    }
    return when {
        hours >= 1 -> String.format("%.0fh %.0fm", hours, minutes)
        minutes >= 1 -> String.format("%.0fm %.0fs", minutes, remainingSeconds)
        else -> String.format("%.0fs", remainingSeconds)
    }
}

