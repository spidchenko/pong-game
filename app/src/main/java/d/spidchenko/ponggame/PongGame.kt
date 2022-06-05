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

    private val mSurfaceHolder: SurfaceHolder = holder
    private lateinit var mCanvas: Canvas
    private val mPaint: Paint = Paint()
    private var mCurrentFPS: Long = 0
    private var mScreenX: Int = 0
    private var mScreenY: Int = 0
    private var mFontSize: Float = 0F
    private var mFontMargin: Float = 0F

    private lateinit var mGameThread: Thread
    private var mPlaying = false
    private var mPaused = true
    private var mSceneInitialized = false

    private lateinit var mBat: Bat
    private lateinit var mBall: Ball

    private var mScore: Int = 0
    private var mTotalLives: Int = 0

    private lateinit var mSoundPool: SoundPool
    private var mBeepId: Int = 0
    private var mBoopId: Int = 0
    private var mBopId: Int = 0
    private var mMissId: Int = 0


    fun pause() {
        Log.d(TAG, "pause")
        mPlaying = false
        mGameThread.join()
    }

    fun resume() {
        Log.d(TAG, "resume")
        mPlaying = true
        // TODO Need to use Kotlin coroutines instead of Threads
        mGameThread = Thread(this)
        mGameThread.start()
    }


    override fun run() {
        Log.d(TAG, "run playing?:$mPlaying   paused?: $mPaused")
        while (mPlaying) {
            val frameStartTime = System.currentTimeMillis()
            if (!mPaused) {
                update()
                detectCollisions()
            }

            draw()

            val timeThisFrame = System.currentTimeMillis() - frameStartTime

            if (timeThisFrame > 0) {
                mCurrentFPS = MILLIS_IN_SECOND / timeThisFrame
            }
        }
    }

    private fun detectCollisions() {

        if (RectF.intersects(mBat.mRect, mBall.mRect)) {
            mBall.batBounce(mBat.mRect)
            mBall.increaseVelocity()
            mScore++
            mSoundPool.play(mBeepId, 1F, 1F, 0, 0, 1F)
        }

        if (mBall.mRect.bottom > mScreenY) {
            mBall.reverseYVelocity()
            mTotalLives--
            mSoundPool.play(mMissId, 1F, 1F,0,0, 1F)
            if (mTotalLives == 0){
                mPaused = true
                startNewGame()
            }
        }

        if (mBall.mRect.top <= 0){
            mBall.reverseYVelocity()
            mSoundPool.play(mBoopId, 1F, 1F,0,0, 1F)
        }

        if (mBall.mRect.left <= 0) {
            mBall.reverseXVelocity()
            mSoundPool.play(mBopId, 1F, 1F,0,0, 1F)
        }

        if (mBall.mRect.right >= mScreenX){
            mBall.reverseXVelocity()
            mSoundPool.play(mBopId, 1F, 1F,0,0, 1F)
        }

    }

    private fun update() {
        mBall.update(mCurrentFPS)
        mBat.update(mCurrentFPS)
    }

    private fun draw() {

        if (holder.surface.isValid) {
            if (!mSceneInitialized) {
                initialize2D()
                startNewGame()
                mSceneInitialized = true
            }

            mCanvas = holder.lockCanvas()
            mCanvas.drawColor(Color.argb(255, 26, 128, 182))
            mPaint.color = Color.WHITE

            mCanvas.drawRect(mBall.mRect, mPaint)
            mCanvas.drawRect(mBat.mRect, mPaint)

            mPaint.textSize = mFontSize
            mCanvas.drawText("Score $mScore   Lives $mTotalLives", mFontMargin, mFontSize, mPaint)
            if (DEBUGGING) {
                printDebuggingText()
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas)
        }
    }

    fun initialize2D(){
        mScreenX = mSurfaceHolder.surfaceFrame.width()
        mScreenY = mSurfaceHolder.surfaceFrame.height()
        mFontSize = mScreenX / 20F
        mFontMargin = mScreenX / 75F
        mBall = Ball(mScreenX)
        mBat = Bat(mScreenX, mScreenY)
    }

    private fun startNewGame() {
        mScore = 0
        mTotalLives = 3
        mBall.reset(mScreenX, mScreenY)
        initializeAudio()
    }

    private fun initializeAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mSoundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        try {
            val assetsManager = context.assets
            var fileDescriptor = assetsManager.openFd("beep.ogg")
            mBeepId = mSoundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("boop.ogg")
            mBoopId = mSoundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("bop.ogg")
            mBopId = mSoundPool.load(fileDescriptor, 0)
            fileDescriptor = assetsManager.openFd("miss.ogg")
            mMissId = mSoundPool.load(fileDescriptor, 0)
        } catch (e: IOException) {
            Log.d(TAG, "initializeAudio: Error: $e")
        }
    }

    private fun printDebuggingText() {
        val debugFontSize = mFontSize / 2F
        mPaint.textSize = debugFontSize
        val leftMargin = 150F
        val topMargin = 10F
        mCanvas.drawText("FPS: $mCurrentFPS", topMargin, leftMargin + debugFontSize, mPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: ")
        if (event != null) {
            mBat.setMovementState(
                when (event.action.and(MotionEvent.ACTION_MASK)) {
                    MotionEvent.ACTION_DOWN -> {
                        mPaused = false
                        if (event.x > (mScreenX / 2)) Bat.RIGHT else Bat.LEFT
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
