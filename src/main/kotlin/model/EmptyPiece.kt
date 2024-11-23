package model

class EmptyPiece: Piece(null) {
   override val isDouble: Boolean = false
   override val isBlack: Boolean get() = false
   override val isWhite: Boolean get() = false
   override val isEmpty: Boolean get() = true
   override fun boardAfterPlay(board: Board,squareFrom: Square, squareTo: Square, middleSquares: List<Square>): BOARD_MAP = board.moves
   override fun canEat(board: Board,squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean = false
   override fun canMove(board: Board,squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean = false
   override fun toString(): String = "EmptyPiece,${this.owner}"
   override fun stillCanEat(board: Board, squareFrom: Square, emptySquares: List<Square>): Boolean = false
}