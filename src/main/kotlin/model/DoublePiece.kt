package model

import kotlin.collections.plus


class DoublePiece(owner: Player) : Piece(owner) {
    override val isBlack: Boolean get() = owner == Player.BLACK
    override val isWhite: Boolean get() = owner == Player.WHITE
    override val isEmpty: Boolean get() = false
    override val isDouble: Boolean = true


override fun boardAfterPlay(board: Board, squareFrom: Square, squareTo: Square, middleSquares: List<Square>): BOARD_MAP {
        val squaresToUpdate = middleSquares.filter { square ->  board.isAdversary(board.squareToContent(square)) }.map {
            it to EmptyPiece()
        }
        val update = squaresToUpdate + listOf(squareFrom to EmptyPiece() , squareTo to  ( DoublePiece(board.turn) ) )
        return board.updatedBoard(update)
    }

    override fun canEat(board: Board, squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean {
        if (middleSquares.isEmpty() ) return false
        val diagonalContents = middleSquares.map { square -> board.squareToContent(square)}
        return (!diagonalContents.any{ piece -> board.isMyPiece(piece) }
                && diagonalContents.filter {piece -> board.isAdversary(piece)}.size == 1)
    }


   override fun canMove(board: Board, squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean {
       return when {
           !Diagonal.isDiagonal(squareFrom, squareTo)  -> false
           middleSquares.isEmpty() -> true
           middleSquares.all { square -> board.squareToContent(square) is EmptyPiece } -> true
           else -> false
       }
   }

    override fun stillCanEat(board: Board, squareFrom: Square, emptySquares: List<Square>): Boolean {
            emptySquares.forEach { emptySquare ->
            if (Diagonal.isDiagonal(squareFrom, emptySquare)) {
            val middleSquares = Diagonal.middleSquares( squareFrom,emptySquare)
            if (canEat(board,squareFrom, emptySquare,middleSquares)) return true} }
        return false
    }
    override fun toString(): String ="DoublePiece,${this.owner}"
 }



