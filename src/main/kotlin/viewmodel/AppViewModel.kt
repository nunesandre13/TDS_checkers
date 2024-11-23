package viewmodel

import view.InputName
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.Board
import model.Game
import model.Multiplayer
import model.MultiplayerRun
import model.Name
import model.Player
import model.Points
import model.Square
import model.newBoard
import model.refresh
import model.start
import mongoDb.MongoDriver
import storage.GameSerializer
import storage.MongoStorage

class AppViewModel(driver: MongoDriver) {

    private val storage =
        MongoStorage<Name, Game>(
            "saves",
            driver,
            GameSerializer
        )

    var multiplayer by mutableStateOf(Multiplayer(storage))   // Model state
    var viewScore by mutableStateOf(false)
    var inputName: InputName? by mutableStateOf<InputName?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    val board: Board get() = (multiplayer as MultiplayerRun).game.board
    val points: Points get() = (multiplayer as MultiplayerRun).game.points
    val hasClash: Boolean get() = multiplayer is MultiplayerRun
    val sidePlayer: Player? get() = (multiplayer as? MultiplayerRun)?.sidePlayer
    val name: Name get() = (multiplayer as MultiplayerRun).id

    fun showScore() {
        viewScore = true
    }

    fun hideScore() {
        viewScore = false
    }

    fun hideError() {
        errorMessage = null
    }

    private fun exec(fx: Multiplayer.() -> Multiplayer): Unit =
        try {
            multiplayer = multiplayer.fx()
        } catch (e: Exception) {
            errorMessage = e.message
        }

    fun refresh() = exec(Multiplayer::refresh)

    fun newBoard(): Unit = exec(Multiplayer::newBoard)

    fun start(name: Name? = null) {
        if (name == null) inputName = InputName.ForStart
        else {
            cancelInput()
            exec { start(name) }
        }
    }

    var selectedSquares by mutableStateOf<List<Square>>(emptyList())

    fun selectSquare(square: Square) {
        selectedSquares = if (selectedSquares.size < 2) {
            selectedSquares + square
        } else {
            listOf(square) // Reseta a seleção se já houver dois quadrados
        }
    }

    fun clearSelection() {
        selectedSquares = emptyList()
    }


    fun cancelInput() {
        inputName = null
    }


}