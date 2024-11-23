
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window

import isel.tds.galo.view.PieceView
import model.BOARD_DIM
import model.BOARD_MAP
import model.Board
import model.Column
import model.Player
import model.Row
import model.SinglePiece
import model.Square


val CELL_SIZE = 100.dp
val GRID_WIDTH = CELL_SIZE * BOARD_DIM  * (BOARD_DIM -1)

@Composable
fun BoardView(moves: BOARD_MAP, onClickCell: (Square) -> Unit) {
    Column(
        modifier = Modifier
            .size(GRID_WIDTH)
            .background(Color.White)
    ) {
        repeat(BOARD_DIM) { row ->
            Row {
                repeat(BOARD_DIM) { col ->
                    val square = Square(Row(row), Column(col))
                    val backgroundColor = if (square.black) Color.Black else Color.White
                    Box(
                        modifier = Modifier
                            .size(CELL_SIZE)
                            .background(backgroundColor)
                    ) {
                        if (square.black) {
                      PieceView(
                            100.dp,
                           moves[square] ?: throw IllegalArgumentException("erro ao aceder a uma peça, provisorio"),
                            onClick = { onClickCell(square) },
                            modifier = Modifier.size(CELL_SIZE).background(Color.Black)
                        )
                     }
                    }
                }
            }
        }
    }
}


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val initialBoard = Board() // Função para inicializar o tabuleiro
        var moves by remember { mutableStateOf(initialBoard.moves) }
        BoardView(
            moves = moves,
            onClickCell = { square ->
                // Atualize o estado do tabuleiro quando uma célula for clicada
                println("Célula clicada: $square")
                moves = moves.toMutableMap().apply {
                    this[square] = SinglePiece(Player.BLACK) // Exemplo de atualização
                }
            }
        )
    }
}

