package storage
import model.Board
import model.Player.Companion.toPlayer
import model.toPiece
import model.toSquare


object BoardSerializer : Serializer<Board> {

    override fun serialize(data: Board): String = buildString {
        append(data.turn)
        append("|")
        data.moves.entries.joinTo(this, separator = " ") { (square, piece) ->
            "$square:$piece"
        }
    }

    override fun deserialize(text: String): Board {
        text.split("|").let { (player, moves)->
            val player = player.toPlayer() ?: error("Invalid Turn")
            val moves = moves.split(" ").associate { entry ->
               entry.split(":").let { (squareStr, pieceStr) ->
                    val square = squareStr.toSquare()
                    pieceStr.split(",").let { (piece, owner) ->
                        val owner = owner.toPlayer()
                        val piece = piece.toPiece(owner) ?: error("Invalid Piece")
                        square to piece } } }
            return Board(moves = moves, turn = player)
        }
    }
}
