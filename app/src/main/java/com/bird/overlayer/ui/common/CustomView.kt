package com.bird.overlayer.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View


@SuppressLint("ClickableViewAccessibility")
class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var targetBitmap: Bitmap? = null
    private var overlayBitmap: Bitmap? = null

    private var gestureListener: GestureListener = GestureListener()
    private var gestureDetector: GestureDetector = GestureDetector(context, gestureListener)
    private var scaleGestureListener: ScaleGestureListener = ScaleGestureListener()
    private var scaleGestureDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, scaleGestureListener)

    private var invalidationForTargetOverlay = false
    private var invalidationForTarget = false

    private var drawOverlayMatrix: Matrix = Matrix()
    private val transformationTargetMatrix = Matrix()

    private val paintTarget = Paint()

    private var lastFocusX = 0f
    private var lastFocusY = 0f

    init {
        setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            scaleGestureDetector.onTouchEvent(event)
        }
    }

    fun setImageBitmap(targetBitmap: Bitmap) {
        this.targetBitmap = targetBitmap
        invalidationForTarget = true
        val originalWidth: Float = targetBitmap.width.toFloat()
        val originalHeight: Float = targetBitmap.height.toFloat()
        val scale = width / originalWidth
        val xTranslation = 0.0f
        val yTranslation = (height - originalHeight * scale) / 2.0f
        transformationTargetMatrix.reset()
        transformationTargetMatrix.postTranslate(xTranslation, yTranslation)
        transformationTargetMatrix.preScale(scale, scale)
        paintTarget.isFilterBitmap = true
        invalidate()
    }


    fun clearOverlay() {
        this.overlayBitmap = null
        drawOverlayMatrix.reset()
        invalidate()
    }

    fun setImageOverlay(overlayBitmap: Bitmap) {
        this.overlayBitmap = overlayBitmap
        invalidationForTargetOverlay = true
        val centerX = (width - overlayBitmap.width.toFloat()) / 2
        val centerY = (height - overlayBitmap.height.toFloat()) / 2
        drawOverlayMatrix.reset()
        drawOverlayMatrix.postTranslate(centerX, centerY)
        invalidationForTargetOverlay = false
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()

        if (targetBitmap != null) {
            canvas?.drawBitmap(
                targetBitmap!!, transformationTargetMatrix, null
            )
            invalidationForTarget = false
        }

        if (overlayBitmap != null) {
            canvas?.drawBitmap(overlayBitmap!!, drawOverlayMatrix, null)
        }

        canvas?.restore()
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            drawOverlayMatrix.postTranslate(-distanceX, -distanceY)
            invalidate()
            return true
        }

        override fun onShowPress(e: MotionEvent) {
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return false
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return false
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return false
        }

    }

    inner class ScaleGestureListener : OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val transformationMatrix = Matrix()
            val focusX = detector.focusX
            val focusY = detector.focusY

            transformationMatrix.postTranslate(-focusX, -focusY)
            transformationMatrix.postScale(
                detector.scaleFactor,
                detector.scaleFactor
            )

            val focusShiftX: Float = focusX - lastFocusX
            val focusShiftY: Float = focusY - lastFocusY
            transformationMatrix.postTranslate(
                focusX + focusShiftX, focusY
                        + focusShiftY
            )
            drawOverlayMatrix.postConcat(transformationMatrix)
            lastFocusX = focusX
            lastFocusY = focusY
            invalidate()
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastFocusX = detector.focusX
            lastFocusY = detector.focusY
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
        }
    }
}