package com.epicdevler.ad.prodigyttt.ui.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GameVM : ViewModel() {

    private val _moves: MutableList<CellState> = MutableList(9) { CellState.Empty }

    private var lastPlayer: Player? = null
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState(cells = _moves))
    val uiState: MutableState<UiState> = mutableStateOf(UiState(cells = _moves))


    fun check() {
        viewModelScope.launch {
            if (lastPlayer != null) {
                val winner = checkWinner(lastPlayer!!)

                if (winner != null) {
                    uiState.value =
                        uiState.value.copy(
                            winner = winner
                        )
                }
            }
        }
    }

    fun invoke(event: Events) {
        val player = uiState.value.currentPlayer
        val players = uiState.value.players
        viewModelScope.launch {
            when (event) {
                is Events.MakeMove -> {
                    val index = event.cellIndex
                    if (_moves[index] == CellState.Empty) {
                        _moves[index] = player!!.cell
                        lastPlayer = player
                        var nextPlayer = players.asList().findLast {
                            it?.cell !=  player.cell
                        }
                        if (nextPlayer == lastPlayer){
                            nextPlayer = players
                                .asList().findLast { it?.cell != player.cell }
                        }
                        val winner = checkWinner(player)
                        uiState.value =
                            uiState.value.copy(
                                cells = _moves,
                                currentPlayer = nextPlayer,
                                winner = winner,
                                isDraw = winner == null && !_moves.contains(CellState.Empty)
                            )

                    }
                }

                Events.Rematch -> {
                    clearCells()
                    val winner = uiState.value.winner
                    val playerOne =
                        if (winner == uiState.value.players.playerOne) uiState.value.players.playerOne?.copy(
                            scores = uiState.value.players.playerOne!!.scores.plus(1)
                        ) else uiState.value.players.playerOne

                    val playerTwo =
                        if (winner == uiState.value.players.playerTwo) uiState.value.players.playerTwo?.copy(
                            scores = uiState.value.players.playerTwo!!.scores.plus(1)
                        ) else uiState.value.players.playerTwo

                    uiState.value = UiState(
                        cells = _moves,
                        currentPlayer = lastPlayer,
                        isDraw = false,
                        players = uiState.value.players.copy(
                            playerOne = playerOne,
                            playerTwo = playerTwo
                        ),

                    )
                }

                Events.Restart -> {
                    clearCells()
                    lastPlayer = null
                    uiState.value = UiState(cells = _moves)
                }

                Events.TogglePause -> {
                    uiState.value = uiState.value.copy(
                        isPaused = !uiState.value.isPaused
                    )
                }
            }
        }
    }

    private fun clearCells() {
        _moves.clear()
        _moves.addAll(MutableList(9) { CellState.Empty })
    }

    private fun checkWinner(player: Player): Player? {
        val v = player.cell
        val moves = _moves

        if(moves.size < 4){
            return null
        }

//            VERTICAL TOP

        if (moves[0] == v && moves[1] == v && moves[2] == v) {
            return player
        }
        //            VERTICAL CENTER
        if (moves[3] == v && moves[4] == v && moves[5] == v) {
            return player
        }
        //            VERTICAL BOTTOM
        if (moves[6] == v && moves[7] == v && moves[8] == v) {
            return player
        }

        //            HORIZONTAL START
        if (moves[0] == v && moves[3] == v && moves[6] == v) {
            return player
        }
        //            HORIZONTAL CENTER
        if (moves[1] == v && moves[4] == v && moves[7] == v) {
            return player
        }
        //            HORIZONTAL END
        if (moves[1] == v && moves[5] == v && moves[8] == v) {
            return player
        }


        //            DIAGONAL TOP_START_BOTTOM_END
        if (moves[0] == v && moves[4] == v && moves[8] == v) {
            return player
        }
        //            DIAGONAL TOP_END_BOTTOM_START
        if (moves[2] == v && moves[4] == v && moves[6] == v) {
            return player
        }

        return null

//        }
    }


    sealed interface Events {
        data object Rematch : Events
        data object Restart : Events
        data object TogglePause : Events

        class MakeMove(val cellIndex: Int) : Events
    }

    data class UiState(
        val players: Players = Players(
            Player(
                name = "Player One",
                cell = CellState.O
            ),
            Player(
                name = "Player Two",
                cell = CellState.X
            )
        ),
        val cells: List<CellState>,
        val currentPlayer: Player? = players.playerOne,
        val winner: Player? = null,
        val isPaused: Boolean = false,
        val isDraw: Boolean = false,
    )

}

data class Players(
    val playerOne: Player? = null,
    val playerTwo: Player? = null
){
    fun asList(): List<Player?> = listOf(
        playerOne,
        playerTwo
    )
}

data class Player(
    val name: String,
    val scores: Int = 0,
    val cell: CellState
)