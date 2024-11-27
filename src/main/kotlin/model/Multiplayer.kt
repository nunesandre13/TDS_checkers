package model
import properties.Config
import storage.MongoStorage


val MINIMAL_PLAYS_TO_JOIN = Config.minimalPlaysToJoin

@JvmInline
value class Name(private val value: String) {
    init { require(isValid(value)) { "Invalid name" } }
    override fun toString() = value
    companion object {
        fun isValid(value: String) =
            value.isNotEmpty() && value.all { it.isLetterOrDigit() } && value.none { it==' ' }
    }
}

typealias GameStorage = MongoStorage<Name, Game>    //Storage<Name, Game>


open class Multiplayer(val st: MongoStorage<Name, Game>)

class MultiplayerRun(
    st: GameStorage,
    val game: Game,
    val principalPlayer: Player,
    val id: Name,
) : Multiplayer(st)


fun Multiplayer.deleteGame() = runOperation {
    st.delete(id)
    game
}

private fun Multiplayer.startGame(id : Name): Multiplayer {
    val game = creatGameRun()
    st.create(id,game)
    return MultiplayerRun(st, game, Player.WHITE, id)
}

private fun Multiplayer.gameAfterJoin(game: Game): GameRun {
    require(game.plays in 0.. MINIMAL_PLAYS_TO_JOIN) {"This Game already exists"}
    require(game is GameRun) { "Game is not a GameRun!" }
    return creatGameRun(game.player.other,game.board,game.plays)
}

private fun Multiplayer.joinGame(id : Name): Multiplayer {
    val game = requireNotNull(st.read(id)) { "Please Try another Name" }
    val assertGame = gameAfterJoin(game)
    return MultiplayerRun(st, assertGame, assertGame.player, id)
}

fun Multiplayer.start(id: Name): Multiplayer {
    return runCatching { startGame(id) }
        .getOrElse { joinGame(id) }
}

private fun Multiplayer.runOperation(operation: MultiplayerRun.()->Game ): Multiplayer {
    check(this is MultiplayerRun) { "Multiplayer not started" }
    return MultiplayerRun(st,operation(),principalPlayer,id)
}

fun Multiplayer.refresh() = runOperation {
    (st.read(id) as Game).also { check(game != it) { "No modification" } }
}

fun Multiplayer.newBoard() = runOperation {
    GameRun().also { st.update(id,it) }
}

fun Multiplayer.play(squareFrom: Square, squareTo: Square) = runOperation {
    check(principalPlayer == (game as GameRun).board.turn) { "Not your turn" }
    game.play(squareFrom,squareTo).also {
        st.update(id,it)
    }
}