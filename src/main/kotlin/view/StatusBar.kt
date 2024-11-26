

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Game
import model.GameDraw
import model.GameRun
import model.GameWin
import model.Player


@Composable
fun StatusBar(game: Game?, gridWidth:Dp, you : Player?) {
    Row(modifier = Modifier.width(gridWidth)
        .background(Color(0xFFFFE4B5))
        .border(2.dp,Color(0xFFFF4500)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        val (text, player) = when(game){
            null -> "Game not started" to null
            is GameRun -> "Turn: " to game.board.turn
            is GameWin -> "Winner: " to game.winner
            is GameDraw -> "Draw" to null
        }
        Text("You ${you?: "Unknown"}", style = MaterialTheme.typography.h4)
        Spacer(Modifier.width(32.dp))
        val displayText = if (player != null) "$text$player" else text
        Text(displayText, fontSize = 32.sp)
    }
}