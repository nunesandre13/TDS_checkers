package storage

import model.Game
import model.GameDraw
import model.GameRun
import model.GameWin
import model.Player.Companion.toPlayer
import model.toPoint

object GameSerializer : Serializer<Game> {
    override fun serialize(data: Game) = buildString {
        append(
            when (data) {
                is GameRun -> "run ${data.player}"
                is GameWin -> "win ${data.winner}"
                is GameDraw -> "draw" })
        append("/")
        append(BoardSerializer.serialize(data.board))
        append("/")
        append(data.points)
        append("/")
        append(data.plays + 1)
    }

    override fun deserialize(text: String): Game {
        text.split("/").let { (gameType, board, points, numberOfPlayers) ->
            val board = BoardSerializer.deserialize(board)
            val points = points.toPoint() ?: error("Invalid Points format")
            val numPlayers = numberOfPlayers.toInt()
            val (type, plyr) = gameType.split(" ")
            return when (type) {
                "run" -> GameRun(board = board, player = plyr.toPlayer() ?: error("Invalid Player"),points,numPlayers)
                "win" -> GameWin(board = board, winner = plyr.toPlayer() ?: error("Invalid Player") ,numPlayers)
                "draw" -> GameDraw(board = board,numPlayers)
                else -> throw IllegalArgumentException("Unknown game type: $gameType")
            }
        }
    }
}

