package com.epicdevler.ad.prodigyttt.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epicdevler.ad.prodigyttt.R
import kotlinx.coroutines.delay


@Composable
fun GameScreen(
    onNavigateUp: () -> Unit,
    onLoad: () -> Unit
) {

    val vm: GameVM = viewModel()

    val uiState = vm.uiState.value

    val currentPlayer = uiState.currentPlayer
    val isPaused = uiState.isPaused
    val isDraw = uiState.isDraw

    LaunchedEffect(Unit){
        delay(1000)
        onLoad()
    }


    if (uiState.winner != null || isPaused || isDraw) {
        val preferredActionColor = ButtonDefaults.elevatedButtonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        )
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = isPaused)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(shapes.large)
                    .background(colorScheme.background, shapes.large)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (isPaused) "Game Paused" else if (isDraw) "Draw" else "${uiState.winner?.name} Wins",
                    textAlign = TextAlign.Center,
                    style = typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElevatedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { vm.invoke(if (isPaused) GameVM.Events.TogglePause else GameVM.Events.Rematch) },
                        colors = if (isPaused || isDraw) preferredActionColor else ButtonDefaults.elevatedButtonColors()
                    ) {
                        Text(text = if (isPaused) "Resume"  else "Rematch")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    ElevatedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { vm.invoke(GameVM.Events.Restart) },
                        colors = if (isPaused || isDraw) ButtonDefaults.elevatedButtonColors() else preferredActionColor
                    ) {
                        Text(text = "New Game")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateUp,
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(text = "Quit Game")
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = { vm.invoke(GameVM.Events.TogglePause) }) {
                Icon(
                    imageVector = if (isPaused) Icons.Rounded.PlayArrow else ImageVector.vectorResource(
                        R.drawable.pause
                    ),
                    contentDescription = null
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Board(
                cells = uiState.cells,
                onCellClicked = { index ->
                    if (uiState.winner == null) {
                        vm.invoke(GameVM.Events.MakeMove(index))
                    }
                }
            )
        }
        PlayersRow(currentPlayer = currentPlayer, uiState.players)
    }
}

@Composable
fun PlayersRow(currentPlayer: Player?, players: Players) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(1f)
    ) {
        players.asList().forEachIndexed { index, player ->
            val containerColor =
                if (currentPlayer == player) colorScheme.primary else colorScheme.secondaryContainer
            val iconColor =
                if (currentPlayer == player) colorScheme.onPrimary else colorScheme.onSecondaryContainer
            val textColor =
                if (currentPlayer == player) colorScheme.primary else colorScheme.onBackground
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            color = containerColor,
                            CircleShape
                        )
                        .padding(10.dp)
                ) {
                    if (player?.cell == CellState.X) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                    if (player?.cell == CellState.O) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.circle),
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "${player?.name} ", color = textColor)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "${player?.scores}", style = typography.displaySmall, color = textColor)
            }
            if (index == 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

enum class CellState {
    X, O, Empty
}

@Composable
fun Board(
    cells: List<CellState>,
    onCellClicked: (index: Int) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(20.dp)
    ) {

        itemsIndexed(cells) { index, state ->
            val shouldHaveTopBoarder = index != 0 && index != 1 && index != 2
            val shouldHaveStartBorder = index != 0 && index != 3 && index != 6
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .bordered(start = shouldHaveStartBorder, top = shouldHaveTopBoarder)
                    .clickable(
                        onClick = {
                            onCellClicked(index)
                        }
                    )
                    .aspectRatio(1f, true)
            ) {
                when (state) {
                    CellState.X -> Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null
                    )

                    CellState.O -> Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.circle),
                        contentDescription = null
                    )

                    CellState.Empty -> Unit
                }
            }
        }
    }

}


fun Modifier.bordered(
    top: Boolean,
    start: Boolean,
): Modifier {
    return composed {
        val color = colorScheme.outline
        val strokeWidth = 3.dp.value
        drawWithContent {
            val width = size.width
            val height = size.width

//                    TOP HORIZONTAL
            if (top) {
                drawLine(
                    color = color,
                    strokeWidth = strokeWidth,
                    start = Offset(0.0f, 0.0f),
                    end = Offset(width, 0.0f),
                    colorFilter = ColorFilter.tint(color)
                )
            }

            if (start) {
                drawLine(
                    color = color,
                    strokeWidth = strokeWidth,
                    start = Offset(0.0f, 0.0f),
                    end = Offset(0.0f, height),
                    colorFilter = ColorFilter.tint(color)
                )
            }
            drawContent()
        }
    }
}
