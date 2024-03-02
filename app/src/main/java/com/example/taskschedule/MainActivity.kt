package com.example.taskschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import com.example.taskschedule.ui.theme.TaskScheduleTheme
import com.example.taskschedule.screens.DatePickerComposable
import com.example.taskschedule.screens.ListaActividadesUI
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskScheduleApp()
        }
    }
}

@Composable
fun TaskScheduleApp() {
    val navController = rememberNavController()
    val viewModel: ActivitiesViewModel = viewModel()

    Scaffold(
        topBar = { TaskBar(navController) },
        bottomBar = { TaskDownBar(navController) }
    ) { innerPadding ->
        // Aplica el innerPadding al contenido para evitar que quede oculto por las barras
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController = navController, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    TopAppBar(
        title = { Text(currentRoute) },//OJO CON EL IDIOMA
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {})
}

@Composable
fun TaskDownBar(navController: NavHostController) {
    // Observa la entrada actual de la pila de navegación para determinar la pantalla activa
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // Define los iconos y rutas para tus botones/iconos
    val items = listOf(
        "listaActividades" to Icons.Filled.List,
        "datePicker" to Icons.Filled.DateRange

    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer, // Color de fondo de la barra
        shadowElevation = 8.dp // Sombra para dar efecto de elevación
    ) {
        Row {
            items.forEach { (route, icon) ->
                IconButton(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (currentRoute == route) MaterialTheme.colorScheme.primary // Color cuando está seleccionado
                        else MaterialTheme.colorScheme.onSurface.copy() // Color cuando no está seleccionado
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: ActivitiesViewModel) {
    NavHost(navController = navController, startDestination = "listaActividades", Modifier.fillMaxSize()) {
        composable("datePicker") {
            DatePickerComposable()
        }
        composable("listaActividades") {
            ListaActividadesUI( viewModel)
        }
    }
}