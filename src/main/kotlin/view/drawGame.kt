package view

import PointsDialog
import StatusBar
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import model.MultiplayerRun
import viewmodel.AppViewModel

@Composable
fun DrawGaming(vm: AppViewModel,gridWidth: Dp ){
    if (vm.hasClash) DrawGame(vm,gridWidth) else DrawEmptyGame(vm,gridWidth)
}


@Composable
fun DrawEmptyGame(vm: AppViewModel,gridWidth: Dp){
    Column() {
        BoardView(null, gridWidth, vm::selectSquare)
        StatusBar(null, gridWidth + calculateCellSize(gridWidth), vm.principalPlayer)
    }
}
@Composable
fun DrawGame(vm: AppViewModel,gridWidth: Dp){
    Column() {
        if (vm.onTarget) BoardView(vm.board, gridWidth, onClickCell = vm::selectSquare, modifierSquares(vm) )
        else  BoardView(vm.board, gridWidth, onClickCell = vm::selectSquare)
        vm.playOrException()
        StatusBar((vm.multiplayer as MultiplayerRun).game, gridWidth + calculateCellSize(gridWidth),vm.principalPlayer)
        if (vm.viewScore) PointsDialog(vm.points,vm.name, onClose = vm::hideScore)
    }
}
