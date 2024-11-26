
import view.StartDialog
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import mongoDb.MongoDriver
import view.DrawGaming
import viewmodel.AppViewModel



@Composable
@Preview
private fun FrameWindowScope.GameApp(driver: MongoDriver, onExit: () -> Unit) {

    var vm: AppViewModel = remember { AppViewModel(driver) }

    var gridWidth by remember { mutableStateOf(500.dp) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8B4513))
            .onSizeChanged { size ->
                // Calcular o tamanho da célula com base no tamanho da janela
                val width = size.width.dp
                val height = size.height.dp
                gridWidth = width.coerceAtMost(height).coerceAtMost(600.dp)
            }
    ) {
        MaterialTheme {
            MenuBar {
                Menu("Game") {
                    Item("Start game", onClick = vm::start)
                    Item("New board", onClick = vm::newBoard)
                    Item("Refresh", enabled = vm.hasClash, onClick = vm::refresh)
                    Item("Show Score", enabled = vm.hasClash, onClick = vm::showScore)
                    Item("Exit", onClick = onExit)
                }
                Menu("Options") {
                    Item("OnTarget",enabled = vm.hasClash, onClick = vm::getTarget)
                }
            }
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    DrawGaming(vm, gridWidth)
                }

            vm.inputName?.let {
                StartDialog(
                    type = it,
                    onCancel = vm::cancelInput,
                    onAction = vm::start
                )
            }
            vm.errorMessage?.let { ErrorDialog(it, onClose = vm::hideError) }
        }
    }
}


const val storageDirectory = "checkers"

fun main() = MongoDriver(storageDirectory).use { driver ->
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(size = DpSize.Unspecified),
            title = "checkers"
        ) {
            GameApp(driver, ::exitApplication)
        }
    }
}