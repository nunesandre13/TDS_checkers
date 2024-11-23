

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import model.Board
import model.Square


@Composable
@Preview
private fun BoardApp() {

    var board: Board by remember { mutableStateOf(Board()) }
    MaterialTheme {
        Column() {
           BoardView(board.moves,
                onClickCell = {
                    sq: Square -> selectSquare(sq)

                }
            )
            if (selectedSquares.size == 2) {
                board = board.play(selectedSquares[0], selectedSquares[1])
                selectedSquares = emptyList()
            }
        }
    }
}




var selectedSquares by mutableStateOf<List<Square>>(emptyList())
    private set

fun selectSquare(square: Square) {
    selectedSquares = if (selectedSquares.size < 2) {
        println(selectedSquares)
        selectedSquares + square

    } else {
        listOf(square)
        // Reseta a seleção se já houver dois quadrados
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(size = DpSize.Unspecified)
    ) {
        BoardApp()
    }
}