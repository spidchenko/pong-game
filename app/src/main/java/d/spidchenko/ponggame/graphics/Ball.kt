package d.spidchenko.ponggame.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import d.spidchenko.ponggame.physics.Vector2d
import kotlin.math.abs

class Ball(screenX: Int): GameObject {
    val rect = RectF()
    private val velocity = Vector2d(true)
    private val position = Vector2d(true)

    private val deltaVelocity = Vector2d(true)
    private val deltaPosition = Vector2d(true)

    private val ballWidth = screenX / 100F
    private val ballHeight = screenX / 100F

    fun update(fps: Long) {

        deltaPosition.apply {
            cloneFrom(velocity)
            divide(fps)
        }
        position.add(deltaPosition)

        deltaVelocity.apply {
            cloneFrom(G)
            divide(fps)
        }
        velocity.add(deltaVelocity)
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

    fun reset(screenWidth: Int, screenHeight: Int) {
        Log.d(TAG, "reset: Ball reset")
        position.set(screenWidth / 2.0, 0.0)
        velocity.set((screenWidth / 2.0), 0.0)
//        velocity.set((screenWidth / 2.0), (-(screenHeight / 3.0)))
    }

    fun increaseVelocity() {
//        velocity.multiply(1.05)
    }

    fun batBounce(batPosition: RectF) {
        val batCenter = batPosition.centerX()
        val ballCenter = rect.centerX()
        val relativeIntersect = batCenter - ballCenter
        if (relativeIntersect < 0) bounceOff(BOUNCE_RIGHT) else bounceOff(BOUNCE_LEFT)
        bounceOff(BOUNCE_UP)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = Color.WHITE
        rect.set(
            position.x.toFloat(), position.y.toFloat(),
            (position.x + ballWidth).toFloat(), (position.y + ballHeight).toFloat()
        )
        canvas.drawRect(rect, paint)
    }

    companion object {
        private const val TAG = "Ball.LOG_TAG"
        const val BOUNCE_LEFT = 0
        const val BOUNCE_RIGHT = 2
        const val BOUNCE_UP = 3
        const val BOUNCE_DOWN = 4

        // Gravity const
        private val G = Vector2d(0.0, 90.0)
    }

}
