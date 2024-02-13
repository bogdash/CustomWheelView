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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.doOnEnd
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlin.random.Random

class WheelView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var animator: ValueAnimator? = null
    private lateinit var canvas: Canvas
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private var resultText = ""
    private var startAngle = 0
    private var sector = 0
    private val paint = Paint()

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { it ->
            when (it.action) {
                MotionEvent.ACTION_UP -> {
                    val initialAngle = startAngle % 360
                    startAngle = Random.nextInt(360, 3600)
                    val newDuration = (startAngle - initialAngle) / 360 * 500L

                    animator = ValueAnimator.ofFloat(initialAngle.toFloat(), startAngle.toFloat())
                    animator?.apply {
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

    private fun showResult(startAngle: Int) {
        sector = (startAngle % 360) / (360f / 7f).toInt()

        when (sector) {
            COLOR_VIOLET -> {
                resultText = "Violet"
                imageView.visibility = GONE
            }

            COLOR_BLUE -> {
                resultText = ""
                setImage()
            }

            COLOR_LIGHT_BLUE -> {
                resultText = "Light Blue"
                imageView.visibility = GONE
            }

            COLOR_GREEN -> {
                resultText = ""
                setImage()
            }

            COLOR_YELLOW -> {
                resultText = "Yellow"
                imageView.visibility = GONE
            }

            COLOR_ORANGE -> {
                resultText = ""
                setImage()
            }

            COLOR_RED -> {
                resultText = "Red"
                imageView.visibility = GONE
            }
        }
        textView.text = resultText

    }

    private fun setImage() {
        imageView.visibility = VISIBLE
        val url = "https://loremflickr.com/320/240"
        try {
            Picasso.get().load(url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }
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

    fun setOnRotationEndListener(imageView: ImageView, textView: TextView) {
        this.imageView = imageView
        this.textView = textView
    }

    fun reset() {
        resultText = ""
        sector = 0
        rotation = 0f
        animator?.cancel()
        imageView.visibility = GONE
    }

    companion object {
        private const val COLOR_VIOLET = 0
        private const val COLOR_BLUE = 1
        private const val COLOR_LIGHT_BLUE = 2
        private const val COLOR_GREEN = 3
        private const val COLOR_YELLOW = 4
        private const val COLOR_ORANGE = 5
        private const val COLOR_RED = 6
    }
}