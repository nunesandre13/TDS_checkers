package isel.tds.galo.view

import GRID_WIDTH
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Game
import model.GameDraw
import model.GameRun
import model.GameWin
import model.Player


@Composable
fun StatusBar(game: Game?, you: Player?) {
    Row(modifier = Modifier.width(GRID_WIDTH)
        .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        you?.let{
            Text("You ", style = MaterialTheme.typography.h5)
            Spacer(Modifier.width(30.dp))
        }
        val (text, player) = when(game){
            null -> "Game not started" to null
            is GameRun -> "Turn: " to game.board.turn
            is GameWin -> "Winner: " to game.winner
            is GameDraw -> "Draw" to null
        }
        Text(text, fontSize = 32.sp)
    }
}