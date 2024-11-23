package model


class Points(
    private val blackPieces: Int = 0 ,
    private val whitePieces: Int = 0) {
    override fun equals(other: Any?): Boolean {
        return (other as Points).blackPieces == blackPieces && other.whitePieces == whitePieces
    }
    fun isEqualPoints(): Boolean = blackPieces == whitePieces

    fun withAddedPoint(player: Player, numberOfPoints: Int): Points {
        return when (player) {
            Player.BLACK -> Points(blackPieces + numberOfPoints, whitePieces)
            Player.WHITE -> Points(blackPieces, whitePieces + numberOfPoints)
        }
    }

    fun pointsOfPlayer(player: Player): Int {
       return when (player) {
            Player.BLACK -> return blackPieces
            Player.WHITE -> return whitePieces
        }
    }

    val totalBlack: Int get() = blackPieces
    val totalWhite: Int get() = whitePieces

    override fun toString(): String {
        return "Black : $totalBlack | WHITE : $totalWhite"
    }
    override fun hashCode(): Int {
        return 31 * blackPieces + whitePieces
    }
}


fun String.toPoint(): Points? {
    val parts = split("|")
    val totalBlack = parts[0].substringAfter("Black :").trim().toIntOrNull() ?: return null
    val totalWhite = parts[1].substringAfter("WHITE :").trim().toIntOrNull() ?: return null
    return Points(totalBlack, totalWhite)
}
/*
fun String.toPoint(): Points? = split("|").let { (blackPieces, whitePieces) ->
        Points(blackPieces.substringAfter("Black :").trim().toIntOrNull() ?: return null,
                whitePieces.substringAfter("White :").trim().toIntOrNull() ?: return null)}

 */