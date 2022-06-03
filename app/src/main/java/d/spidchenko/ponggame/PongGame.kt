package d.spidchenko.ponggame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

class PongGame(context: Context) : SurfaceView(context) {

    private val DEBUGGING = true
    private val MILLIS_IN_SECOND = 1000
    private val surfaceHolder: SurfaceHolder = holder
    private val canvas: Canvas
    private val paint: Paint = Paint()
    private var currentFPS: Long = 0
    private val screenX: Int = surfaceHolder.surfaceFrame.width()
    private val screenY: Int = surfaceHolder.surfaceFrame.height()
    private val fontSize: Int = screenX / 20
    private val fontMargin: Int = screenX / 75

    private val bat: Bat
    private val ball: Ball

    private val score: Int
    private val totalLives: Int

    init {
        startNewGame();
    }

    private fun startNewGame() {
        TODO("Not yet implemented")
    }
}
