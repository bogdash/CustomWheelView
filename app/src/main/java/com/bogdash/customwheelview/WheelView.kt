package com.bogdash.customwheelview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd
import kotlin.random.Random

class WheelView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var resultText = ""
    private val paint = Paint()
    private var startAngle = 0
    private lateinit var animator: ValueAnimator
    private lateinit var canvas: Canvas



    private val colors = listOf(
        Color.RED,
        Color.rgb(255, 127, 0), // orange
        Color.YELLOW,
        Color.GREEN,
        Color.rgb(0, 191, 255), // light blue
        Color.BLUE,
        Color.rgb(148, 0, 211) // violet
    )

    private val sweepAngle = 360f / colors.size

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        drawCircleRainbow(canvas)
    }


    private fun drawCircleRainbow(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = width / 2f
        val radius = width.coerceAtMost(height) / 2f

        for (i in colors.indices) {
            paint.color = colors[i]
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                startAngle + i * sweepAngle,
                sweepAngle,
                true,
                paint
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { it ->
            when (it.action) {
                MotionEvent.ACTION_UP -> {
                    invalidate()
                    val initialAngle = startAngle % 360
                    startAngle = Random.nextInt(360, 3600)
                    val newDuration = (startAngle - initialAngle) / 360 * 1000L

                    animator = ValueAnimator.ofFloat(initialAngle.toFloat(), startAngle.toFloat())
                    animator.apply {
                        duration = newDuration
                        addUpdateListener {
                            rotation = it.animatedValue as Float
                        }
                        doOnEnd {
                            showResult(startAngle)
                        }
                        start()
                    }
                }

                else -> {}
            }
        }
        return true
    }

    override fun invalidate() {
        super.invalidate()
        if (resultText.isNotEmpty()) {
            resultText = ""
        }
    }

    private fun showResult(startAngle: Int) {
        val sector = (startAngle % 360) / (360f / 7f).toInt()

        when(sector){
            0 -> resultText = "Violet"
            1 -> resultText = "Blue"
            2 -> resultText = "Light Blue"
            3 -> resultText = "Green"
            4 -> resultText = "Yellow"
            5 -> resultText = "Orange"
            6 -> resultText = "Red"
        }
        drawText(canvas)
    }
    private fun drawText(canvas: Canvas) {

        paint.textSize = 50f
        val textWidth = paint.measureText(resultText)
        val fontMetrics = paint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top

        val textX = (width - textWidth) / 2
        val textY = (height - textHeight) / 2

        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(resultText, textX, textY, paint)
    }
}