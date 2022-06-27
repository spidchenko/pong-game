package d.spidchenko.ponggame

import android.graphics.RectF

class Bat(private val screenX: Int, screenY: Int) {
    private val batSpeed = screenX
    private val length = screenX / 8F
    private val height = screenY / 40F
    private var xCoord = screenX / 2F
    private val yCoord = screenY - height
    private var batMoving = STOPPED

    val rect = RectF(xCoord, yCoord, xCoord + length, yCoord + height)

    fun setMovementState(state: Int?) {
        if (state != null) {
            batMoving = state
        }
    }

    fun update(fps: Long) {

        xCoord = when (batMoving) {
            LEFT -> xCoord - (batSpeed / fps)
            RIGHT -> xCoord + (batSpeed / fps)
            else -> xCoord
        }

        when {
            (xCoord < 0) -> xCoord = 0F
            (xCoord + length > screenX) -> xCoord = screenX - length
        }

        rect.left = xCoord
        rect.right = xCoord + length
    }

    companion object {
        const val STOPPED = 0
        const val LEFT = 1
        const val RIGHT = 2
    }

}
