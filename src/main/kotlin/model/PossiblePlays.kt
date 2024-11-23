package model

data class PossiblePlays(
    val possibleEat: List<Pair<Square, Square>>,
    val possibleMoves: List<Pair<Square, Square>>) {
    companion object {
        fun calculate(board: Board): PossiblePlays {
            val myPieces = board.moves.filter { it.value.owner == board.turn }
            val emptyPieces = board.moves.filter { it.value is EmptyPiece }
            val possibleEat = mutableListOf<Pair<Square, Square>>()
            val possibleMoves = mutableListOf<Pair<Square, Square>>()
            myPieces.forEach { piece ->
                emptyPieces.forEach { emptyPiece ->
                    val middleSquare = Diagonal.middleSquares(piece.key, emptyPiece.key)
                    if (piece.value.canEat(board, piece.key, emptyPiece.key, middleSquare)) {
                        possibleEat += piece.key to emptyPiece.key
                    } else if (possibleEat.isEmpty() && piece.value.canMove(
                            board,
                            piece.key,
                            emptyPiece.key,
                            middleSquare
                        )
                    ) {
                        possibleMoves += piece.key to emptyPiece.key
                    }
                }
            }
            return PossiblePlays(
                possibleEat = possibleEat,
                possibleMoves = possibleMoves
            )
        }

        fun PossiblePlays.emptyPlays(): Boolean =
            possibleMoves.isEmpty() && possibleEat.isEmpty()
    }
}
