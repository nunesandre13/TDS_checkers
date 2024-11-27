package viewmodel
import view.InputName
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
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

    private val boardTurn: Player get() = board.turn

    val points: Points get() = (multiplayer as MultiplayerRun).game.points

    val hasClash: Boolean get() = multiplayer is MultiplayerRun

    val principalPlayer: Player? get() = (multiplayer as? MultiplayerRun)?.principalPlayer

    val name: Name get() = (multiplayer as MultiplayerRun).id

    var selectedSquares: SelectionState by mutableStateOf(SelectionState.None)

    var autoRefresh: Boolean by mutableStateOf(false)

    sealed class SelectionState {
        data object None : SelectionState()
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

    private fun isMyPiece(square: Square) = principalPlayer == board.moves[square]!!.owner


    fun refresh() = exec(Multiplayer::refresh)

    fun newBoard(): Unit = exec(Multiplayer::newBoard)

    fun deleteGameFromStorage() = exec(Multiplayer::deleteGame)

    private var autoRefreshJob: Job? = null

    fun autoRefresh(intervalMillis: Long = 5000) {
        autoRefreshJob?.cancel()
        autoRefresh = true
        // Inicia uma nova coroutine para executar o refresh periodicamente
        autoRefreshJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) { // Verifica se a coroutine ainda está ativa
                try {
                    multiplayer = multiplayer.refresh() // Chama o método de refresh
                } catch (e: Exception) {
                   // errorMessage = "Error during auto-refresh: ${e.message}"
                }
                delay(intervalMillis) // Espera o intervalo definido
            }
        }
    }

    fun stopAutoRefresh() {
        autoRefresh = false
        autoRefreshJob?.cancel() // Cancela a coroutine de auto-refresh
        autoRefreshJob = null
    }

    fun startOrEndRefresh(intervalMillis: Long = 5000) {
        if (autoRefreshJob == null) {
            autoRefresh(intervalMillis) // Inicia o auto-refresh
        } else {
            stopAutoRefresh() // Para o auto-refresh
        }
    }

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
        selectedSquares = when {
           selectedSquares is SelectionState.None -> SelectionState.FirstSelected(square)
            selectedSquares is SelectionState.FirstSelected  && isMyPiece(square) -> SelectionState.FirstSelected(square)
            selectedSquares is SelectionState.FirstSelected -> SelectionState.BothSelected((selectedSquares as SelectionState.FirstSelected).from, square)
            else -> SelectionState.None
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