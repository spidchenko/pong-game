package d.spidchenko.ponggame.graphics

import android.graphics.Canvas
import android.graphics.Paint

interface GameObject {
    fun draw(canvas: Canvas, paint: Paint)
}