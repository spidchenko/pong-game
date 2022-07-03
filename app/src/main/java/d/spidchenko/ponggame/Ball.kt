package d.spidchenko.ponggame

import android.graphics.RectF
import android.util.Log
import d.spidchenko.ponggame.physics.Vector2d
import kotlin.math.abs

class Ball(screenX: Int) {
    val rect = RectF()
    private val velocity = Vector2d(true)
    private val ballWidth = screenX / 100F
    private val ballHeight = screenX / 100F

    fun update(fps: Long) {
//        Log.d(
//            TAG,
//            "updating ball. fps=$fps xVel=$xVelocity yVel=$yVelocity left=${rect.left} right = ${rect.right}"
//        )

        rect.left += (velocity.x / fps).toFloat()
        rect.top += (velocity.y / fps).toFloat()
        rect.right = rect.left + ballWidth
        rect.bottom = rect.top + ballHeight
    }

    fun bounceOff(direction: Int) {
        when (direction) {
            BOUNCE_LEFT -> velocity.x = -abs(velocity.x)
            BOUNCE_RIGHT -> velocity.x = abs(velocity.x)
            BOUNCE_UP -> velocity.y = -abs(velocity.y)
            BOUNCE_DOWN -> velocity.y = abs(velocity.y)
            else -> throw IllegalArgumentException("Unknown direction")
        }
    }

    fun reset(x: Int, y: Int) {
        Log.d(TAG, "reset: Ball reset")
        rect.left = x / 2F
        rect.top = 0F
        rect.right = x / 2F + ballWidth
        rect.bottom = ballHeight

        velocity.y = -(y / 3.0)
        velocity.x = x / 2.0
    }

    fun increaseVelocity() {
        velocity.x *= 1.1F
        velocity.y *= 1.1F
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

        // Gravity const
        const val G = 9.8
    }

}
