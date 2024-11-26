package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun SquareView(backgroundColor: Color, size: Dp, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit = {}) =
    Box(modifier = modifier.size(size).background(backgroundColor),content = content)