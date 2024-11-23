package model

@JvmInline
value class Row (val index: Int){

    init {
        require(index in 0 until BOARD_DIM) { "Invalid row index: $index" }
    }


    val digit: Char
        get() = ('0' + BOARD_DIM) - index

    companion object {
        val values = (0 until BOARD_DIM).map{Row(it) }
    }
}

fun Char.toRowOrNull(): Row? {
    return if (this in '1'..'0' + BOARD_DIM) Row( BOARD_DIM - this.digitToInt() )  else null
}


fun main() {
    val a:List<Row> = List(BOARD_DIM) { Row(it) }
    for (row in a) {
        println(row.digit)
    }
    println(a)
}