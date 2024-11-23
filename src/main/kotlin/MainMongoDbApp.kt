
import view.StartDialog
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import isel.tds.galo.view.ErrorDialog
import model.play
import mongoDb.MongoDriver
import viewmodel.AppViewModel


@Composable
@Preview
private fun FrameWindowScope.GridApp(driver: MongoDriver, onExit: () -> Unit) {

    var vm: AppViewModel = remember { AppViewModel(driver) }

    MaterialTheme {
        MenuBar {
            Menu("Game") {
                Item("Start game", onClick = vm::start)
                Item("New board", onClick = vm::newBoard)
                Item("Refresh", enabled = vm.hasClash, onClick = vm::refresh)
                Item("Show Score", onClick = vm::showScore)
                Item("Exit", onClick = onExit)
            }
        }
        Column() {
            if (vm.hasClash) {
                BoardView(vm.board.moves, onClickCell = vm::selectSquare)
                if (vm.selectedSquares.size == 2) {
                   vm.multiplayer =  vm.multiplayer.play(vm.selectedSquares[0], vm.selectedSquares[1])
                    vm.selectedSquares = emptyList()
                }
                //StatusBar(vm.board, vm.sidePlayer)
            }
            if (vm.viewScore) ScoreDialog(vm.points, vm.sidePlayer!!, onClose = vm::hideScore)
        }
        vm.inputName?.let {
            StartDialog(
            type = it,
                onCancel = vm::cancelInput,
                onAction=  vm::start
        ) }
        vm.errorMessage?.let { ErrorDialog(it, onClose = vm::hideError) }
    }
}

const val storageDirectory = "checkers"
fun main() = MongoDriver(storageDirectory).use { driver ->
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(size = DpSize.Unspecified),
            title = "ck"
        ) {
            GridApp(driver, ::exitApplication)
        }
    }
}