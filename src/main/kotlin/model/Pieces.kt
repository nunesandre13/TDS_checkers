package model



abstract  class Piece(val owner: Player?) {
    abstract val isBlack: Boolean
    abstract val isWhite: Boolean
    abstract val isEmpty: Boolean
    abstract val isDouble: Boolean
    abstract fun canEat(board: Board,squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean
    abstract fun stillCanEat(board: Board, squareFrom: Square, emptySquares: List<Square>): Boolean
    abstract fun canMove(board: Board,squareFrom: Square, squareTo: Square, middleSquares: List<Square>): Boolean
    abstract fun boardAfterPlay(board: Board,squareFrom: Square, squareTo: Square, middleSquares: List<Square>): BOARD_MAP
    abstract override fun toString(): String
    override fun equals(other: Any?): Boolean {
        if (this === other) return true // verifica se é o mesmo objeto
        if (other !is Piece) return false // verifica se é uma instância de SinglePiece
        return owner == other.owner // compara o dono
    }

    override fun hashCode(): Int {
        return owner?.hashCode() ?: 0
    }

}

fun String.toPiece(owner: Player?): Piece? =
    if (owner == null ){
        if (this == "EmptyPiece" ) EmptyPiece()
         else null
    } else
        when(this){"SinglePiece" -> SinglePiece(owner)"DoublePiece" -> DoublePiece(owner) else  -> null }