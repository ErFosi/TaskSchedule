package com.example.taskschedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskschedule.screens.DatePickerComposable
import com.example.taskschedule.screens.ListaActividadesUI
import com.example.taskschedule.screens.LanguageAndThemeSelector
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.compose.TaskSchedule
import com.example.taskschedule.viewmodels.ActivitiesViewModel
import com.example.taskschedule.viewmodels.CalendarViewModel

//import com.example.taskschedule.ui.theme.TaskScheduleTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<ActivitiesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create the Authentication Notification Channel
        val name = getString(R.string.channel_act)
        val descriptionText = getString(R.string.channel_desc)

        val mChannel = NotificationChannel("Task_channel", name, NotificationManager.IMPORTANCE_LOW)
        mChannel.description = descriptionText
        onNewIntent(intent)
        // Register the channel with the system
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        setContent {
            TaskSchedule(useDarkTheme  = viewModel.oscuro.collectAsState(initial = true).value) {
                TaskScheduleApp(viewModel = viewModel)
            }

        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var context=this
        Log.d("T", "Se va a activar el botón")
        intent?.getIntExtra("id", -1)?.let { id ->
            if (id != -1) {
                Log.d("T", "El id es $id")
                viewModel.viewModelScope.launch {
                    viewModel.obtenerActividadPorId(id).firstOrNull()?.let { actividad ->
                        viewModel.togglePlay(actividad = actividad, context)
                    } ?: Log.d("E", "Ha habido un error")
                }
            }
        }
    }
}

@Composable
fun TaskScheduleApp(viewModel: ActivitiesViewModel) {
    val navController = rememberNavController()
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
    var text=""
    if (currentRoute.equals("listaActividades")){
        text= stringResource(id = R.string.Lista)
    }
    else if(currentRoute.equals("datePicker")){
        text= stringResource(id = R.string.Estadísticas)
    }
    else if(currentRoute.equals("settings")){
        text= stringResource(id = R.string.settings)
    }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text)

                Spacer(Modifier.width(8.dp))

                when (currentRoute) {
                    "listaActividades" -> {

                        Icon(Icons.Filled.List, contentDescription = null)
                    }
                    "datePicker" -> {
                        // Ícono no pulsable para las estadísticas
                        Icon(Icons.Filled.BarChart, contentDescription = null)
                    }
                    "settings" -> {
                        // Ícono no pulsable para las estadísticas
                        Icon(Icons.Filled.Settings, contentDescription = null)
                    }

                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {},
        actions = {
            IconButton(onClick = {navController.navigate("settings"){
                // Configura el lanzador para evitar múltiples instancias de la misma pantalla en el back stack
                launchSingleTop = true
                // Opcional: Puedes restaurar el estado cuando reingresas a una pantalla desde el back stack
                restoreState = false
            }}) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )
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
                    onClick = {if (currentRoute != route) {navController.navigate(route){
                        // Configura el lanzador para evitar múltiples instancias de la misma pantalla en el back stack
                        launchSingleTop = true
                        // Opcional: Puedes restaurar el estado cuando reingresas a una pantalla desde el back stack
                        restoreState = false
                    }}  },
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
            val calendarViewModel: CalendarViewModel = hiltViewModel()
            DatePickerComposable(calendarViewModel = calendarViewModel)
        }

        composable("listaActividades") {
            ListaActividadesUI( viewModel)
        }
        composable(route="settings"){
            LanguageAndThemeSelector(actividadesViewModel = viewModel)
        }
    }
}
