package model



object Diagonal {

   private fun mod(number1: Int, number2: Int): Int {
        val minus = number1 - number2
        return if (minus > 0) minus else -minus
    }

    fun isDiagonal(squareFrom: Square, squareTo: Square): Boolean {
        return mod(squareFrom.column.index, squareTo.column.index) == mod(squareFrom.row.index, squareTo.row.index)
    }

    fun middleSquares(squareFrom: Square, squareTo: Square): List<Square> {
        if (!isDiagonal(squareFrom, squareTo)) return emptyList()
        val move : Direction = Direction.getDirection(squareFrom, squareTo)
        val steps = (1 until mod(squareFrom.row.index , squareTo.row.index))
        return steps.map { step ->
            Square(Row(squareFrom.row.index + step * move.rowStep), Column(squareFrom.column.index + step * move.colStep))
        }
    }
}

