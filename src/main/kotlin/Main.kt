import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication(
    title = "Asteroids ComposeJB",
    resizable = false,
) {

    val game = remember { Game() }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                game.update(it)
            }
        }
    }

    Column(modifier = Modifier
        .background(Color.DarkGray)
        .fillMaxWidth()
        .fillMaxHeight()
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button({
                game.startGame()
            }) {
                Text("Play")
            }
            Text(
                game.gameStatus,
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 16.dp),
                color = Color.White
            )
        }
        Box(
            modifier = Modifier
                .aspectRatio(1.0f)
                .background(Color.Black)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clipToBounds()
                .onPointerEvent(PointerEventType.Move) {
                    with(it.changes.first().position) {
                        game.targetLocation = DpOffset(x.dp, y.dp)
                    }
                }
                .onPointerEvent(PointerEventType.Press) {
                    game.ship.fire(game)
                }
                .onSizeChanged {
                        game.width = it.width.dp
                        game.height = it.height.dp
                }) {
                game.gameObjects.forEach {
                    when (it) {
                        is ShipData -> Ship(it)
                        is BulletData -> Bullet(it)
                        is AsteroidData -> Asteroid(it)
                        else -> {}
                    }
                }
            }
        }
    }
}
