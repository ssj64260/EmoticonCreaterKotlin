package com.android.enoticoncreaterkotlin.util

import android.content.res.AssetManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import com.android.enoticoncreaterkotlin.model.GifTheme
import pl.droidsonroids.gif.GifDrawable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class GifHelper {

    companion object {
        private val strokeWidth = 1f//字体边框宽度
        private val padding = 5//内边距
        private val textColor = 0xfffafafa.toInt()

        fun create(assetManager: AssetManager, theme: GifTheme, savePath: String, typeface: Typeface): File? {
            try {
                val gifFileName = theme.fileName
                val textSize = theme.textSize
                val duration = theme.duration
                val textList = theme.textList

                val drawable = GifDrawable(assetManager, gifFileName!!)
                val frames = drawable.numberOfFrames
                val encoder = AnimatedGifEncoder()
                val os = ByteArrayOutputStream()

                val paint = Paint()

                encoder.setRepeat(0)
                encoder.setDelay(duration)
                encoder.start(os)

                for (i in 0 until frames) {
                    val bitmap = drawable.seekToFrameAndGet(i)
                    if (textList != null && !textList.isEmpty()) {
                        for (gifText in textList) {
                            val text = gifText.text
                            val startFrame = gifText.startFrame
                            val endFrame = gifText.endFrame

                            if (i in startFrame..(endFrame - 1)) {
                                val canvas = Canvas(bitmap)
                                val textTop = bitmap.height.toFloat() - padding.toFloat() - textSize
                                val maxWidth = bitmap.width - padding * 2

                                initTextPaint(paint, textSize, textColor, true, typeface)
                                drawText(canvas, paint, text!!, textTop, maxWidth)

                                initTextPaint(paint, textSize, 0x99000000.toInt(), false, typeface)
                                drawText(canvas, paint, text, textTop, maxWidth)
                                break
                            }
                        }
                    }

                    encoder.addFrame(bitmap)
                }

                encoder.finish()
                drawable.recycle()

                val fileName = System.currentTimeMillis().toString() + ".gif"

                return ImageUtils.saveToGif(os, savePath, fileName)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        private fun initTextPaint(paint: Paint, textSize: Float, textColor: Int, isFill: Boolean, typeface: Typeface) {
            paint.reset()
            if (isFill) {
                paint.style = Paint.Style.FILL
            } else {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = strokeWidth
                paint.strokeCap = Paint.Cap.ROUND
            }
            paint.color = textColor
            paint.textSize = textSize
            paint.flags = Paint.ANTI_ALIAS_FLAG
            paint.typeface = typeface
        }

        private fun drawText(canvas: Canvas, paint: Paint, text: String, top: Float, maxWidth: Int) {
            val textRect = Rect()
            paint.getTextBounds(text, 0, text.length, textRect)
            val textWidth = textRect.right

            val textTop = top - textRect.top
            val textLeft = (maxWidth - textWidth) / 2f
            canvas.drawText(text, textLeft, textTop, paint)
        }
    }

}