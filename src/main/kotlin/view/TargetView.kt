package view

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Square
import viewmodel.AppViewModel

@Composable
fun modifierSquares(vm: AppViewModel): (Square) -> Modifier {
    val mySquare = mySquare(vm)
    val highlighted = highlightedSquares(vm)
    return { square ->
        when (square) {
            mySquare -> Modifier.border(3.dp, Color.Red)
            in highlighted -> Modifier.drawWithContent{
                drawContent()
                drawCircle(  color = Color.Green,
                    radius = size.minDimension / 3,
                    center = center)
            }
            else -> Modifier
        }
    }
}

fun highlightedSquares(vm: AppViewModel): List<Square> {
    val mySquare = mySquare(vm)
    val squaresMoves = vm.board.possiblePlaysData.possibleMoves
    val squaresEaten = vm.board.possiblePlaysData.possibleEat
    return if (mySquare == null) getSquaresWithoutTarget(squaresMoves, squaresEaten)
    else getSquaresWithTarget(squaresMoves,squaresEaten,mySquare)
}

fun getSquaresWithoutTarget(squaresMoves: List<Pair<Square, Square>>, squaresEaten: List<Pair<Square, Square>>) :List<Square> {
   return if (squaresMoves.isEmpty()) squaresEaten.map { it.second }
    else squaresMoves.map { it.second }
}

fun getSquaresWithTarget(squaresMoves: List<Pair<Square, Square>>, squaresEaten: List<Pair<Square, Square>>, mySquare: Square): List<Square> {
   return if (squaresMoves.isEmpty()) squaresEaten.filter { pair -> pair.first == mySquare }.map { it.second }
    else squaresMoves.filter { pair -> pair.first == mySquare }.map { it.second }
}

fun mySquare(vm: AppViewModel): Square? {
    return when (val state = vm.selectedSquares) {
        is AppViewModel.SelectionState.FirstSelected -> state.from
        is AppViewModel.SelectionState.BothSelected -> state.from
        else -> null
    }
}








//fun DrawBoardOnTarget(vm : AppViewModel, gridWidth: Dp, onClickCell: (Square) -> Unit) {
//        val cellSize = calculateCellSize(gridWidth)
//        Column(modifier = Modifier.size(gridWidth)) {
//            repeat(BOARD_DIM) { row ->
//                Row {
//                    repeat(BOARD_DIM) { col ->
//                        val square = Square(model.Row(row), model.Column(col))
//                        val backgroundColor = getBackGroundColor(vm,square)
//                        SquareView(backgroundColor, cellSize) {
//                            drawPieceInBlackSquare(square, vm.board, cellSize, onClickCell)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//fun getBackGroundColor(vm : AppViewModel, square: Square) : Color {
//    val mySquare = vm.selectedSquares.first
//    val teste = vm.board!!.possiblePlaysData
//   return when {
//         mySquare == square -> return Color.Red
//         mySquare to square in teste.possibleMoves || mySquare to square in teste.possibleEat  -> return Color.Green
//        else  -> if (square.black) Color.Black else Color.White
//    }
//}
