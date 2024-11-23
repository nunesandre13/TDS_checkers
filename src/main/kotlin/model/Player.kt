package model

enum class Player{
    WHITE,BLACK ;
    val other get() = if(this == WHITE) BLACK else WHITE
    override fun toString(): String {
        return this.name
    }
    companion object {
        fun String.toPlayer() : Player? {
            return when (this) {
                "WHITE" -> WHITE
                "BLACK" -> BLACK
                else -> null
            }
        }
    }
}

