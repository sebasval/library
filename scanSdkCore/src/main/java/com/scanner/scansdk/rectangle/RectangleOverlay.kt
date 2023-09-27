package com.scanner.scansdk.rectangle

import android.content.Context
import android.util.AttributeSet
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import org.opencv.core.Mat

class RectangleOverlay : View {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private val paint = Paint()

    private fun init() {
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
    }

    var corners: FloatArray? = null  // Cambiado de Mat a FloatArray

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        corners?.let {
            // Aquí, convierte las esquinas del FloatArray en un Rect o en cuatro puntos y dibuja el rectángulo
            // Por ejemplo:
            val x1 = it[0]
            val y1 = it[1]
            val x2 = it[2]
            val y2 = it[3]
            val x3 = it[4]
            val y3 = it[5]
            val x4 = it[6]
            val y4 = it[7]

            // Dibuja las líneas entre los puntos
            canvas.drawLine(x1, y1, x2, y2, paint)
            canvas.drawLine(x2, y2, x3, y3, paint)
            canvas.drawLine(x3, y3, x4, y4, paint)
            canvas.drawLine(x4, y4, x1, y1, paint)
        }
    }
}

