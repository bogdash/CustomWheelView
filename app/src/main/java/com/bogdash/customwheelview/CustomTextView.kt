package com.bogdash.customwheelview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class CustomTextView(context: Context, attributeSet: AttributeSet) : TextView(context, attributeSet) {
    private val paint = Paint()

    init {
        typeface = Typeface.DEFAULT_BOLD
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.BLACK
    }
}