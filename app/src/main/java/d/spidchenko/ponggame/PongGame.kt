package d.spidchenko.ponggame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

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

    private lateinit var bat: Bat
    private lateinit var ball: Ball

    private var score: Int = 0
    private var totalLives: Int = 0

    private lateinit var soundPool: SoundPool
    private var beepId: Int = 0
    private var boopId: Int = 0
    private var bopId: Int = 0
    private var missId: Int = 0


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
                update()
                detectCollisions()
            }

            draw()

            val timeThisFrame = System.currentTimeMillis() - frameStartTime

            if (timeThisFrame > 0) {
                currentFPS = MILLIS_IN_SECOND / timeThisFrame
            }
        }
    }

    private fun detectCollisions() {

        when{
            RectF.intersects(bat.rect, ball.rect) -> {
                ball.batBounce(bat.rect)
                ball.increaseVelocity()
                score++
                soundPool.play(beepId, 1F, 1F, 0, 0, 1F)
            }

            ball.rect.bottom > screenY -> {
                ball.bounceOff(Ball.BOUNCE_UP)
                totalLives--
                soundPool.play(missId, 1F, 1F,0,0, 1F)
                if (totalLives == 0){
                    isPaused = true
                    startNewGame()
                }
            }

            ball.rect.top < 0 -> {
                ball.bounceOff(Ball.BOUNCE_DOWN)
                soundPool.play(boopId, 1F, 1F,0,0, 1F)
            }

            ball.rect.left < 0 -> {
                ball.bounceOff(Ball.BOUNCE_RIGHT)
                soundPool.play(bopId, 1F, 1F,0,0, 1F)
            }

            ball.rect.right >= screenX -> {
                ball.bounceOff(Ball.BOUNCE_LEFT)
                soundPool.play(bopId, 1F, 1F,0,0, 1F)
            }
        }
    }

    private fun update() {
        ball.update(currentFPS)
        bat.update(currentFPS)
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

            canvas.drawRect(ball.rect, paint)
            canvas.drawRect(bat.rect, paint)

            paint.textSize = fontSize
            canvas.drawText("Score $score   Lives $totalLives", fontMargin, fontSize, paint)
            if (DEBUGGING) {
                printDebuggingText()
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun initialize2D(){
        screenX = surfaceHolder.surfaceFrame.width()
        screenY = surfaceHolder.surfaceFrame.height()
        fontSize = screenX / 20F
        fontMargin = screenX / 75F
        ball = Ball(screenX)
        bat = Bat(screenX, screenY)
    }

    private fun startNewGame() {
        score = 0
        totalLives = 3
        ball.reset(screenX, screenY)
        initializeAudio()
    }

    private fun initializeAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        try {
            val assetsManager = context.assets
            var fileDescriptor = assetsManager.openFd("beep.ogg")
            beepId = soundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("boop.ogg")
            boopId = soundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("bop.ogg")
            bopId = soundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("miss.ogg")
            missId = soundPool.load(fileDescriptor, 0)
        } catch (e: IOException) {
            Log.d(TAG, "initializeAudio: Error: $e")
        }
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
