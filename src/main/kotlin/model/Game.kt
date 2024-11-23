package model

import model.PossiblePlays.Companion.emptyPlays
import properties.Config

val CHECKERS_POINTS = Config.checkersPoints

sealed class Game(val board: Board, val points: Points = Points(), val plays: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Game) return false
        return board == other.board && points == other.points
    }

    override fun hashCode(): Int {
        return 31 * board.hashCode() + points.hashCode()
    }
}

class GameRun(board: Board = Board(), val player: Player = Player.WHITE, points: Points = Points(), plays: Int = 0) : Game(board, points, plays) {
    fun checkWin(newBoard: Board, points: Points): Boolean =
        newBoard.possiblePlaysData.emptyPlays() && points.pointsOfPlayer(player) == NUM_PIECES

    fun checkDraw(newBoard: Board, points: Points): Boolean =
        newBoard.possiblePlaysData.emptyPlays() && points.isEqualPoints()
}

class GameWin(board: Board, val winner: Player, plays: Int) : Game(board, Points(), plays)

class GameDraw(board: Board, plays: Int) : Game(board, Points(), plays)

fun Game.play(squareFrom: Square, squareTo: Square): Game =
    when (this) {
        is GameRun -> {
            val updatedBoard = board.play(squareFrom, squareTo)
            val updatedPoints = if (board.possiblePlaysData.possibleEat.contains(squareFrom to squareTo)) {
                points.withAddedPoint(board.turn,CHECKERS_POINTS)
            } else points
            when {
                checkWin(updatedBoard,updatedPoints) -> GameWin(updatedBoard, player, plays)
                checkDraw(updatedBoard,updatedPoints) -> GameDraw(updatedBoard, plays)
                else -> GameRun(updatedBoard, player, updatedPoints, plays)
            }
        }
        is GameWin, is GameDraw -> error("Game over")
    }

fun creatGameRun(player: Player = Player.WHITE, board: Board = Board(),plays: Int = 0): GameRun{
   return GameRun(board, player, plays = plays)
}