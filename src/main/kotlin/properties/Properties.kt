package properties

import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

object Config {
    private val properties: Properties = Properties()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        try {
            FileInputStream("src/main/resources/.properties").use { input ->
                properties.load(input)
            }
        } catch (ex: IOException) {
            println("Error Loading the Properties: ${ex.message}")
        }
    }

    val boardDim : Int
        get() = properties.getProperty("board_dim","8").toInt()

    val numPieces: Int
        get() = properties.getProperty("num_pieces","12" ).toInt()

    val minimalPlaysToJoin: Int
        get() = properties.getProperty("minimal_plays_to_join","1" ).toInt()

    val checkersPoints : Int
        get() = properties.getProperty("checkers_points","1").toInt()
}