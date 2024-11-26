

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import model.Name
import model.Points


@Composable
fun PointsDialog(points: Points, gameName: Name,onClose: () -> Unit) =
    DialogBase("Points of $gameName Game", onClose) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$points",
                            style = MaterialTheme.typography.h6)
                    }
            }
        }
    }