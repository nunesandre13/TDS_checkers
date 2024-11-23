package model

enum class Direction(val rowStep: Int, val colStep: Int) {
    UP_RIGHT(-1,1),
    UP_LEFT(-1,-1),
    DOWN_LEFT(1,-1),
    DOWN_RIGHT(1,1);
companion object {
    fun getDirection(squareFrom: Square, squareTo: Square): Direction {

        val rowStep = (squareFrom.row.index < squareTo.row.index) //Direction.DOWN else Direction.UP

        val colStep = (squareFrom.column.index < squareTo.column.index)// Direction.RIGHT else Direction.LEFT

        return when {
            rowStep && colStep -> DOWN_RIGHT
            rowStep && !colStep -> DOWN_LEFT
            !rowStep && colStep -> UP_RIGHT
            else -> UP_LEFT
         }
        }
    }
}