package model

class SinglePiece(owner: Player) : Piece(owner) {
    override val isDouble: Boolean = false
    override val isBlack: Boolean get() = owner == Player.BLACK
    override val isWhite: Boolean get() = owner == Player.WHITE
    override val isEmpty: Boolean get() = false

    override fun boardAfterPlay(board: Board, squareFrom: Square, squareTo: Square, middleSquares: List<Square>): BOARD_MAP {
        val changes = mutableListOf(
            squareFrom to EmptyPiece(),
            squareTo to if(board.pieceToDouble(squareTo)) DoublePiece(board.turn) else SinglePiece(board.turn))
        if (middleSquares.isNotEmpty()) {
            changes.add(middleSquares.first() to EmptyPiece())
        }
        return board.updatedBoard(changes)
    }

    override fun stillCanEat(board: Board, squareFrom: Square, emptySquares: List<Square>): Boolean {
        emptySquares.forEach { emptySquare ->
                val middleSquares = Diagonal.middleSquares( squareFrom,emptySquare)
                if (canEat(board,squareFrom, emptySquare,middleSquares)) return true
        }
        return false
    }

    override fun canEat(board: Board, squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean {
        val middleSquare = middleSquares.singleOrNull() ?: return false
        return board.isAdversary(board.squareToContent(middleSquare))
                && board.isMovingForward(squareFrom,squareTo)
    }

   override fun canMove(board: Board, squareFrom: Square, squareTo: Square, middleSquares: List<Square>) =
      (Diagonal.isDiagonal(squareFrom,squareTo) && middleSquares.isEmpty() && board.squareToContent(squareTo) is  EmptyPiece
              && board.isMovingForward(squareFrom,squareTo))

    private fun Board.isMovingForward(squareFrom: Square, squareTo: Square): Boolean {
        when{
            turnIsBlack -> if (squareTo.index < squareFrom.index ) return false
            turnIsWhite -> if (squareTo.index > squareFrom.index ) return false
        }
        return true
    }

   private fun Board.pieceToDouble(squareTo: Square): Boolean {
        return when (turn){
            Player.BLACK -> squareTo.row.digit == '1'
            Player.WHITE -> squareTo.row.digit == '0' + BOARD_DIM
        }
    }
    override fun toString(): String = "SinglePiece,${this.owner}"
}



