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
import model.deleteGame
import model.newBoard
import model.play
import model.refresh
import model.start
import mongoDb.MongoDriver
import storage.GameSerializer
import storage.MongoStorage

class AppViewModel(driver: MongoDriver) {

    private val storage = MongoStorage<Name, Game>("saves", driver, GameSerializer)

    var onTarget : Boolean by mutableStateOf(false)

    var multiplayer by mutableStateOf(Multiplayer(storage))

    var viewScore by mutableStateOf(false)

    var inputName: InputName? by mutableStateOf<InputName?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val board: Board get() = (multiplayer as MultiplayerRun).game.board

    val boardTurn: Player get() = board.turn

    val points: Points get() = (multiplayer as MultiplayerRun).game.points

    val hasClash: Boolean get() = multiplayer is MultiplayerRun

    val principalPlayer: Player? get() = (multiplayer as? MultiplayerRun)?.principalPlayer

    val name: Name get() = (multiplayer as MultiplayerRun).id

    var selectedSquares: SelectionState by mutableStateOf(SelectionState.None)

    sealed class SelectionState {
        object None : SelectionState()
        data class FirstSelected(val from: Square) : SelectionState()
        data class BothSelected(val from: Square, val to: Square) : SelectionState()
    }

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

    fun deleteGameFromStorage() = exec(Multiplayer::deleteGame)


    private fun play() {
      if( selectedSquares is SelectionState.BothSelected) {
              val first = (selectedSquares as SelectionState.BothSelected).from
              val second = (selectedSquares as SelectionState.BothSelected).to
              multiplayer = multiplayer.play(first, second)
              getTarget()
              selectedSquares = SelectionState.None
          }
      }


    fun playOrException(){
        try {
            play()
        }catch (e: Exception){
            selectedSquares = SelectionState.None
            errorMessage = e.message
        }
    }


    fun getTarget(){
        onTarget = !onTarget && boardTurn == principalPlayer
    }

    fun selectSquare(square: Square) {
        selectedSquares = when (selectedSquares) {
            is SelectionState.None -> SelectionState.FirstSelected(square)
            is SelectionState.FirstSelected -> SelectionState.BothSelected((selectedSquares as SelectionState.FirstSelected).from, square)
            is SelectionState.BothSelected -> SelectionState.None // Resetar seleção
        }
    }

    fun start(name: Name? = null) {
        if (name == null) inputName = InputName.ForStart
        else {
            cancelInput()
            exec { start(name) }
        }
    }

    fun cancelInput() {
        inputName = null
    }
}