package d.spidchenko.ponggame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

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
    private var mPlaying = false;
    private var mPaused = true
    private var mSceneInitialized = false

    //    private val mBat: Bat
    private var mBall: Ball? = null

    private var mScore: Int = 0
    private var mTotalLives: Int = 0


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

    }

    private fun update() {
        mBall?.update(mCurrentFPS)
    }

    private fun draw() {

        if (holder.surface.isValid) {
            if (!mSceneInitialized) {
                startNewGame()
                mSceneInitialized = false
            }

            mCanvas = holder.lockCanvas()
            mCanvas.drawColor(Color.argb(255, 26, 128, 182))
            mPaint.color = Color.WHITE

            mBall?.mRect?.let { mCanvas.drawRect(it, mPaint) }

            mPaint.textSize = mFontSize
            mCanvas.drawText("Score $mScore   Lives $mTotalLives", mFontMargin, mFontSize, mPaint)
            if (DEBUGGING) {
                printDebuggingText()
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas)
        }
    }

    private fun startNewGame() {
        mScreenX = mSurfaceHolder.surfaceFrame.width()
        mScreenY = mSurfaceHolder.surfaceFrame.height()
        mFontSize = mScreenX / 20F
        mFontMargin = mScreenX / 75F

        mScore = 0
        mTotalLives = 3

        mBall = Ball(mScreenX)
        mBall?.reset(mScreenX, mScreenY)
    }

    private fun printDebuggingText() {
        val debugFontSize = mFontSize / 2F
        val leftMargin = 150F
        val topMargin = 10F
        mCanvas.drawText("FPS: $mCurrentFPS", topMargin, leftMargin + debugFontSize, mPaint)
    }

    companion object {
        private const val TAG = "PongGame.LOG_TAG"
        private const val DEBUGGING = true
        private const val MILLIS_IN_SECOND = 1000
    }
}
