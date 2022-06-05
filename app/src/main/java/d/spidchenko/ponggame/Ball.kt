package d.spidchenko.ponggame

import android.graphics.RectF
import android.util.Log
import kotlin.math.abs

class Ball(screenX: Int) {
    val mRect = RectF()
    private var mXVelocity = 0F
    private var mYVelocity = 0F
    private val mBallWidth = screenX / 100F
    private val mBallHeight = screenX / 100F

    fun update(fps: Long) {
        Log.d(TAG, "updating ball. fps=$fps xVel=$mXVelocity yVel=$mYVelocity")
        mRect.left += mXVelocity / fps
        mRect.top += mYVelocity / fps
        mRect.right = mRect.left + mBallWidth
        mRect.bottom = mRect.top + mBallHeight
    }

    fun reverseXVelocity() {
        mXVelocity = -mXVelocity
    }

    fun reverseYVelocity() {
        mYVelocity = -mYVelocity
    }

    fun reset(x: Int, y: Int) {
        mRect.left = x / 2F
        mRect.top = 0F
        mRect.right = x / 2F + mBallWidth
        mRect.bottom = mBallHeight

        mYVelocity = -(y / 3F)
        mXVelocity = x / 2F
    }

    fun increaseVelocity() {
        mXVelocity *= 1.1F
        mYVelocity *= 1.1F
    }

    fun batBounce(batPosition: RectF) {
        val batCenter = batPosition.centerX()
        val ballCenter = mRect.centerX()
        val relativeIntersect = batCenter - ballCenter

        mXVelocity = if (relativeIntersect < 0) {
            abs(mXVelocity)     // Go right
        } else {
            -abs(mXVelocity)    //Go left
        }
        reverseYVelocity()
    }

    companion object{
        private const val TAG = "Ball.LOG_TAG"
    }

}
