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
        Log.d(
            TAG,
            "updating ball. fps=$fps xVel=$mXVelocity yVel=$mYVelocity left=${mRect.left} right = ${mRect.right}"
        )
        mRect.left += (mXVelocity / fps)
        mRect.top += (mYVelocity / fps)
        mRect.right = mRect.left + mBallWidth
        mRect.bottom = mRect.top + mBallHeight
    }

    fun bounceOff(direction: Int) {
        when (direction) {
            BOUNCE_LEFT -> mXVelocity = -abs(mXVelocity)
            BOUNCE_RIGHT -> mXVelocity = abs(mXVelocity)
            BOUNCE_UP -> mYVelocity = -abs(mYVelocity)
            BOUNCE_DOWN -> mYVelocity = abs(mYVelocity)
            else -> throw IllegalArgumentException("Unknown direction")
        }
    }

    fun reset(x: Int, y: Int) {
        Log.d(TAG, "reset: Ball reset")
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
        if (relativeIntersect < 0) bounceOff(BOUNCE_RIGHT) else bounceOff(BOUNCE_LEFT)
        bounceOff(BOUNCE_UP)
    }

    companion object {
        private const val TAG = "Ball.LOG_TAG"
        const val BOUNCE_LEFT = 0
        const val BOUNCE_RIGHT = 2
        const val BOUNCE_UP = 3
        const val BOUNCE_DOWN = 4
        const val BOUNCE_FROM_BAT = 5
    }

}
