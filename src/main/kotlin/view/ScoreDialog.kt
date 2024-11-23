

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import isel.tds.galo.view.DialogBase
import model.Player
import model.Points


@Composable
fun ScoreDialog(points: Points, player: Player, onClose: () -> Unit) =
    DialogBase("Score in $player", onClose) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Column {
                Player.entries.forEach { player ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = " - ${points.pointsOfPlayer(player)}",
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }
            Text(
                text = "Draws }",
                style = MaterialTheme.typography.h6
            )
        }
    }