package model

class Square private constructor (val index : Int) {

    val row get() = Row(index / BOARD_DIM)

    val column get() = Column(index % BOARD_DIM)

    val black = (row.index + column.index) % 2 == 1

    val white  = !black

    companion object{
      val values = (0 until (BOARD_DIM * BOARD_DIM)).map { Square(it) }
  }

    override fun toString(): String {
        return "${row.digit}${column.symbol}"
    }
}


fun Square(row: Row, column: Column): Square {
    return Square.values[row.index * BOARD_DIM + column.index % BOARD_DIM]
}

 fun String.toSquareOrNull(): Square? {
    if (this.length != 2) return null
    val row = this[0].toRowOrNull() ?: return null
    val column = this[1].toColumnOrNull() ?: return null
    return Square(row, column)
}

fun String.toSquare(): Square {
    val square = this.toSquareOrNull()
    return if (square == null) throw IllegalArgumentException("Invalid square representation: $this") else square
}
