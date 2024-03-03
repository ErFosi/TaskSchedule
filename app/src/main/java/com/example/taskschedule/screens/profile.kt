package com.example.taskschedule.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskschedule.ActivitiesViewModel
import com.example.taskschedule.R
import com.example.taskschedule.data.Idioma
import com.example.taskschedule.ui.theme.TaskScheduleTheme


@Composable
fun LanguageAndThemeSelector(actividadesViewModel: ActivitiesViewModel) {
    var idioma=""
    idioma=stringResource(id = R.string.idioma)
    var selectedLanguage by remember { mutableStateOf(idioma) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var expanded by remember{ mutableStateOf(false) }
    val idiomas = listOf("Español", "Euskera", "English")
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(id = R.string.selecciona), fontWeight = FontWeight.Bold)
        Icon(Icons.Filled.ArrowDropDown, contentDescription = stringResource(id = R.string.idioma))
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(selectedLanguage, fontWeight = FontWeight.Bold)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                idiomas.forEach { idioma ->
                    DropdownMenuItem(
                        text = { Text(idioma,fontWeight = FontWeight.Bold) },
                        onClick = {
                            selectedLanguage=idioma
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(id = R.string.claro), fontWeight = FontWeight.Bold)
                IconButton(onClick = { actividadesViewModel.cambiarOscuro(false)}, modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)) {
                    Icon(Icons.Filled.LightMode, contentDescription = stringResource(id = R.string.claro))
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(id = R.string.oscuro), fontWeight = FontWeight.Bold)
                IconButton(onClick = { actividadesViewModel.cambiarOscuro(true) }, modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)) {
                    Icon(Icons.Filled.DarkMode, contentDescription = stringResource(id = R.string.oscuro))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLanguageAndThemeSelector() {
    TaskScheduleTheme {
        //LanguageAndThemeSelector()
    }
}