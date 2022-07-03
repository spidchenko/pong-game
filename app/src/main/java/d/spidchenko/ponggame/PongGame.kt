package d.spidchenko.ponggame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import d.spidchenko.ponggame.graphics.Ball
import d.spidchenko.ponggame.graphics.Bat
import d.spidchenko.ponggame.physics.PhysicsEngine

class PongGame(context: Context) : SurfaceView(context), Runnable {

    private val surfaceHolder: SurfaceHolder = holder
    private lateinit var canvas: Canvas
    private val paint: Paint = Paint()
    private var currentFPS: Long = 0
    private var screenX: Int = 0
    private var screenY: Int = 0
    private var fontSize: Float = 0F
    private var fontMargin: Float = 0F

    private lateinit var gameThread: Thread
    private var isPlaying = false
    private var isPaused = true
    private var isSceneInitialized = false

    private val physicsEngine = PhysicsEngine()
    private val gameState = GameState(context)
    private val soundEngine = SoundEngine(context)


    fun pause() {
        Log.d(TAG, "pause")
        isPlaying = false
        gameThread.join()
    }

    fun resume() {
        Log.d(TAG, "resume")
        isPlaying = true
        // TODO Need to use Kotlin coroutines instead of Threads
        gameThread = Thread(this)
        gameThread.start()
    }


    override fun run() {
        Log.d(TAG, "run playing?:$isPlaying   paused?: $isPaused")
        while (isPlaying) {
            val frameStartTime = System.currentTimeMillis()
            if (!isPaused) {
                physicsEngine.update(currentFPS, soundEngine, gameState)
            }

            draw()

            val timeThisFrame = System.currentTimeMillis() - frameStartTime

            if (timeThisFrame > 0) {
                currentFPS = MILLIS_IN_SECOND / timeThisFrame
            }
        }
    }

    private fun draw() {

        if (holder.surface.isValid) {
            if (!isSceneInitialized) {
                initialize2D()
                startNewGame()
                isSceneInitialized = true
            }

            canvas = holder.lockCanvas()
            canvas.drawColor(Color.argb(255, 26, 128, 182))
            paint.color = Color.WHITE

            gameState.gameObjects.forEach { it.draw(canvas, paint) }

            paint.textSize = fontSize
            canvas.drawText(
                "Score ${gameState.score}   Lives ${gameState.totalLives}",
                fontMargin,
                fontSize,
                paint
            )
            if (DEBUGGING) {
                printDebuggingText()
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun initialize2D() {
        screenX = surfaceHolder.surfaceFrame.width()
        screenY = surfaceHolder.surfaceFrame.height()
        gameState.newGame(screenX, screenY)
        fontSize = screenX / 20F
        fontMargin = screenX / 75F
    }

    private fun startNewGame() {
//        score = 0
//        totalLives = 3000
//        ball.reset(screenX, screenY)
    }

    private fun printDebuggingText() {
        val debugFontSize = fontSize / 2F
        paint.textSize = debugFontSize
        val leftMargin = 150F
        val topMargin = 10F
        canvas.drawText("FPS: $currentFPS", topMargin, leftMargin + debugFontSize, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: ")
        if (event != null) {
            val bat = gameState.gameObjects[0] as Bat
            bat.setMovementState(
                when (event.action.and(MotionEvent.ACTION_MASK)) {
                    MotionEvent.ACTION_DOWN -> {
                        isPaused = false
                        if (event.x > (screenX / 2)) Bat.RIGHT else Bat.LEFT
                    }
                    MotionEvent.ACTION_UP -> Bat.STOPPED
                    else -> null
                }
            )
        }
        return true
    }

    companion object {
        private const val TAG = "PongGame.LOG_TAG"
        private const val DEBUGGING = true
        private const val MILLIS_IN_SECOND = 1000
    }
}
