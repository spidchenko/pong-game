package d.spidchenko.ponggame

import android.graphics.RectF

class Bat(screenX: Int, screenY: Int) {
    private val mScreenX = screenX
    private val mBatSpeed = mScreenX
    private val mLength = mScreenX / 8F
    private val mHeight = screenY / 40F
    private var mXCoord = screenX / 2F
    private val mYCoord = screenY - mHeight
    private var mBatMoving = STOPPED

    val mRect = RectF(mXCoord, mYCoord, mXCoord + mLength, mYCoord + mHeight)

    fun setMovementState(state: Int?) {
        if (state != null) {
            mBatMoving = state
        }
    }

    fun update(fps: Long) {

        mXCoord = when (mBatMoving) {
            LEFT -> mXCoord - (mBatSpeed / fps)
            RIGHT -> mXCoord + (mBatSpeed / fps)
            else -> mXCoord
        }

        when {
            (mXCoord < 0) -> mXCoord = 0F
            (mXCoord + mLength > mScreenX) -> mXCoord = mScreenX - mLength
        }

        mRect.left = mXCoord
        mRect.right = mXCoord + mLength
    }

    companion object {
        const val STOPPED = 0
        const val LEFT = 1
        const val RIGHT = 2
    }

}
