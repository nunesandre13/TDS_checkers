package model


 @JvmInline
 value class Column(val index: Int) {

    init {
        require(index in 0 until BOARD_DIM) { "Invalid column index: $index" }
    }


    val symbol: Char
        get() = 'a' + index


    companion object {
        val values = (0 until BOARD_DIM).map{Column(it) }


    }

}

fun Char.toColumnOrNull(): Column? {
    val char = this.lowercaseChar()
    return if (char in 'a'..'a' + BOARD_DIM - 1) Column(char - 'a')  else null
}

fun main() {
    val a:List<Column> = List(BOARD_DIM) { Column(it) }
    for (row in a) {
        println(row.symbol)
    }
    println(a)
}
