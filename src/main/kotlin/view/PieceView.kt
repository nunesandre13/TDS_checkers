package isel.tds.galo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.DoublePiece
import model.Piece
import model.SinglePiece

@Composable
fun PieceView (
    size: Dp= 100.dp,
    piece: Piece,
    onClick: ()->Unit = {},
    modifier: Modifier = Modifier.size(size)
    ) {
    val filename = pieceToPNG(piece)
    Box(modifier.clickable(onClick = onClick))
    if (filename != null) {
        Image(
            painter = painterResource("$filename.png"),
            contentDescription = "Piece $piece $filename",
            modifier = modifier
        )
        }
    }



fun pieceToPNG(piece: Piece): String?= when(piece){
        is SinglePiece -> if (piece.isBlack)  "piece_black" else  "piece_white"
        is DoublePiece -> if (piece.isBlack)  "double_black" else  "double_white"
       else -> null}
