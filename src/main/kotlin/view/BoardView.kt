package view
import PieceView
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.BOARD_DIM
import model.Board
import model.Column
import model.Row
import model.Square





@Composable
fun BoardView(board: Board?, gridWidth: Dp, onClickCell: (Square) -> Unit, modifierForSquare: ((Square) -> Modifier) = { Modifier} ){
    val cellSize = calculateCellSize(gridWidth)
    Row {
        DrawRowNumbers(cellSize)
        Column {
            DrawColumnNumbers(cellSize)
            DrawBoard(board, gridWidth, onClickCell,modifierForSquare)
        }
    }
}

@Composable
fun DrawColumnNumbers(cellSize: Dp) {
    Row{
        repeat(BOARD_DIM) { col ->
            Box(
                modifier = Modifier
                    .size(cellSize)
                    .background(Color(0xFFFFE4B5))
                    .border(2.dp,Color(0xFFFF4500)),
                contentAlignment = Alignment.Center
            ) {
                Text(('A' + col).toString(), style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Composable
fun DrawRowNumbers(cellSize: Dp) {
    Column {
        Spacer(modifier = Modifier.height(cellSize)) // Espaço no canto superior esquerdo
        repeat(BOARD_DIM) { row ->
            Box(
                modifier = Modifier
                    .size(cellSize)
                    .background(Color(0xFFFFE4B5))
                    .border(2.dp,Color(0xFFFF4500)),
                contentAlignment = Alignment.Center
            ) {
                Text((BOARD_DIM - row).toString(), style = MaterialTheme.typography.body1)

            }
        }
    }
}

@Composable
fun DrawBoard(board: Board?, gridWidth: Dp, onClickCell: (Square) -> Unit, modifierForSquare: ((Square) -> Modifier) = { Modifier }) {
    val cellSize = calculateCellSize(gridWidth)
    Box(
        modifier = Modifier.
        border(2.dp,Color.Black)
    ) {
        Column(modifier = Modifier.size(gridWidth)) {
            repeat(BOARD_DIM) { row ->
                Row {
                    repeat(BOARD_DIM) { col ->
                        val square = Square(Row(row), Column(col))
                        val backgroundColor = if (square.black) Color.Black else Color.White
                        SquareView(backgroundColor, cellSize, modifierForSquare(square)) {
                            drawPieceInBlackSquare(square, board, cellSize, onClickCell)
                        }
                    }
                }
            }
        }
    }
}



fun calculateCellSize(gridWidth: Dp): Dp {
    return gridWidth / BOARD_DIM
}


@Composable
fun drawPieceInBlackSquare(square: Square,board: Board?,cellSize :Dp,onClickCell: (Square) -> Unit ) {
    if (square.black && board != null) {
        PieceView(cellSize, board.squareToContent(square),
            onClick = { onClickCell(square) },
            modifier = Modifier.size(cellSize).background(Color.Black)
        )
    }
}

@Preview
@Composable
fun GridPreview() {
    DrawBoard(Board(), 500.dp,{})
}
