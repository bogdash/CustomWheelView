package com.bogdash.customwheelview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd
import kotlin.random.Random

class WheelView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    private var startAngle = 0
    private lateinit var animator: ValueAnimator

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

                        }
                        start()
                    }
                }

                else -> {}
            }
        }
        return true
    }
}