package d.spidchenko.ponggame

import android.graphics.RectF
import android.util.Log
import kotlin.math.abs

class Ball(screenX: Int) {
    val rect = RectF()
    private var xVelocity = 0F
    private var yVelocity = 0F
    private val ballWidth = screenX / 100F
    private val ballHeight = screenX / 100F

    fun update(fps: Long) {
        Log.d(
            TAG,
            "updating ball. fps=$fps xVel=$xVelocity yVel=$yVelocity left=${rect.left} right = ${rect.right}"
        )
        rect.left += (xVelocity / fps)
        rect.top += (yVelocity / fps)
        rect.right = rect.left + ballWidth
        rect.bottom = rect.top + ballHeight
    }

    fun bounceOff(direction: Int) {
        when (direction) {
            BOUNCE_LEFT -> xVelocity = -abs(xVelocity)
            BOUNCE_RIGHT -> xVelocity = abs(xVelocity)
            BOUNCE_UP -> yVelocity = -abs(yVelocity)
            BOUNCE_DOWN -> yVelocity = abs(yVelocity)
            else -> throw IllegalArgumentException("Unknown direction")
        }
    }

    fun reset(x: Int, y: Int) {
        Log.d(TAG, "reset: Ball reset")
        rect.left = x / 2F
        rect.top = 0F
        rect.right = x / 2F + ballWidth
        rect.bottom = ballHeight

        yVelocity = -(y / 3F)
        xVelocity = x / 2F
    }

    fun increaseVelocity() {
        xVelocity *= 1.1F
        yVelocity *= 1.1F
    }

    fun batBounce(batPosition: RectF) {
        val batCenter = batPosition.centerX()
        val ballCenter = rect.centerX()
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
    }

}
