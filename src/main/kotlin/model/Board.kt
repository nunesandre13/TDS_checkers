package model

import properties.Config


val BOARD_DIM = Config.boardDim
val NUM_PIECES = Config.numPieces
val ROW_PIECES = NUM_PIECES / (BOARD_DIM / 2)

typealias BOARD_MAP = Map<Square, Piece>

class Board(
    val moves: BOARD_MAP = Square.values.filter { it.black }.associateWith {
        when (it.index) {
            in 0..ROW_PIECES * BOARD_DIM - 1 -> SinglePiece(Player.BLACK)
            in (BOARD_DIM * (BOARD_DIM - ROW_PIECES))..BOARD_DIM * BOARD_DIM - 1 -> SinglePiece(Player.WHITE)
            else -> EmptyPiece()
        }
    },
    val turn: Player = Player.WHITE)
{
    val possiblePlaysData : PossiblePlays = PossiblePlays.calculate(this)

    private val possibleEat get() = possiblePlaysData.possibleEat

    private val possibleMoves get() = possiblePlaysData.possibleMoves

    val turnIsBlack: Boolean get() = turn == Player.BLACK

    val turnIsWhite: Boolean get() = turn == Player.WHITE

    fun squareToContent(square: Square): Piece {
        return moves[square] ?: throw IllegalArgumentException("No piece found for this square")
    }
   fun isAdversary(piece: Piece): Boolean {
       if (piece is EmptyPiece) return false
        return when (turn) {
            Player.WHITE -> piece.owner == Player.BLACK
            Player.BLACK -> piece.owner == Player.WHITE
        }
    }

    fun isMyPiece(piece: Piece): Boolean {
        return  piece.owner == turn
    }

    fun updatedBoard(changes: List<Pair<Square, Piece>>): BOARD_MAP {
        return changes.fold(moves) { updatedMoves, (square, piece) ->
            updatedMoves + (square to piece)
        }
    }

    override fun equals(other: Any?): Boolean = moves == (other as Board).moves && turn == other.turn

    override fun hashCode(): Int {
        val initialValue = moves.hashCode()
        val list = listOf(turn, possibleEat, possibleMoves, turnIsBlack, turnIsWhite)
        return list.fold(initialValue) { result, elem -> 31 * result + (elem.hashCode()) }
    }

    fun play(squareFrom: Square, squareTo: Square): Board {
        val movingPiece = squareToContent(squareFrom)
        validatePlay(movingPiece, squareFrom, squareTo)
        val middleSquares = Diagonal.middleSquares(squareFrom, squareTo)
        if (squareFrom to squareTo in possibleEat) {
            val emptySquares = emptySquares().filter { it !in middleSquares }
            if (movingPiece.stillCanEat(this, squareTo, emptySquares)) {
                println("Play not completed; additional capture possible.")
                return playMove(squareFrom, squareTo, turn, middleSquares) }
        }
        return playMove(squareFrom, squareTo, turn.other,middleSquares)
    }

    private fun validatePlay(movingPiece: Piece, squareFrom: Square, squareTo: Square) {
        require(!isAdversary(movingPiece)) { "Not your turn: Player -> ${turn.other}" }
        require(squareToContent(squareTo) is EmptyPiece) { "Destination is not empty: $squareTo" }
        require(possibleEat.isEmpty() || (squareFrom to squareTo in possibleEat)) {
            "Capture is mandatory: you must eat an opponent's piece." }
        require(squareFrom to squareTo in possibleEat || squareFrom to squareTo in possibleMoves) { "No possible moves for this square" }
    }

    private fun emptySquares(): List<Square> {
        return moves.filter { it.value is EmptyPiece }.map { it.key }
    }

    private fun playMove(squareFrom: Square, squareTo: Square, turn: Player, middleSquares: List<Square>): Board {
        val movingPiece = squareToContent(squareFrom)
        return Board(movingPiece.boardAfterPlay(this, squareFrom, squareTo, middleSquares), turn)
    }
}

