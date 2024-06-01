package edu.uoc.rng2.ui.screens.bestGamesResult

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uoc.rng2.R
import edu.uoc.rng2.models.GameResult
import edu.uoc.rng2.ui.formattedDate
import edu.uoc.rng2.ui.icon
import edu.uoc.rng2.ui.iconColor
import edu.uoc.rng2.ui.title

// Función componible para mostrar la pantalla de historial de resultados.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestGames(
    viewModel: BestGamesViewModel, // ViewModel para la pantalla de historial.
    onBack: () -> Unit, // Función de callback para volver atrás.
) {
    // Obteniendo la lista de resultados del juego del ViewModel.
    val bestGamesState by viewModel.bestGameState.collectAsState()

    // Estructura principal de la pantalla.
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Barra superior de la pantalla.
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.best_games)) // Título de la barra superior.
                },
                navigationIcon = {
                    // Icono de navegación para volver atrás.
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, // Icono de flecha hacia atrás.
                            contentDescription = null,
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AnimatedContent(bestGamesState) { state ->
                when (state) {
                    BestGamesState.Error -> Text(text = stringResource(R.string.best_games_load_failed))
                    is BestGamesState.Loaded -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.games) { game ->
                                Column (modifier = Modifier.padding(16.dp)) {
                                    Text("${game.name} <${game.email}>")
                                    Text(stringResource(R.string.win_in, game.turns))
                                }
                            }
                        }
                    }
                    BestGamesState.Loading -> CircularProgressIndicator()
                }
            }
        }
    }
}

// Función componible para mostrar un resultado del juego en la lista.
@Composable
private fun GameResult(gameResult: GameResult, onGameResultClick: () -> Unit) {
    // Estructura para mostrar cada resultado del juego en la lista.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGameResultClick() } // Hacer clic en un resultado del juego.
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icono del resultado del juego.
        Icon(
            painterResource(id = gameResult.icon),
            contentDescription = null,
            tint = gameResult.iconColor,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),  // Espaciado del icono.
        )

        // Detalles del resultado del juego (título y fecha).
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                gameResult.title, // Título del resultado del juego.
                fontSize = 18.sp,
            )

            Text(
                gameResult.formattedDate, // Fecha formateada del resultado del juego.
            )

            val lat = gameResult.latitude
            val long = gameResult.longitude

            if (lat != null && long != null) {
                Text(
                    "Lat: $lat",
                )

                Text(
                    "Long: $long",
                )
            }


        }
    }
}
