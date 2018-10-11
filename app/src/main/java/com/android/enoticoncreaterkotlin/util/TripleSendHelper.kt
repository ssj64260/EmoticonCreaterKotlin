package com.android.enoticoncreaterkotlin.util

import android.graphics.*
import java.io.File

class TripleSendHelper private constructor(builder: Builder) {

    private val backgroundWidth = 640
    private val backgroundHeight = 300
    private val backgroundColor = -0x1
    private val textColor = -0xf4f4f5
    private val textSize = 30
    private val pictureWidth = 200
    private val pictureHeight = 200
    private val textHeight = 50
    private val padding = 10

    private val title: String
    private val path1: String
    private val path2: String
    private val path3: String
    private val name1: String
    private val name2: String
    private val name3: String
    private val savePath: String
    private val typeface: Typeface

    init {
        this.title = builder.title!!
        this.path1 = builder.path1!!
        this.path2 = builder.path2!!
        this.path3 = builder.path3!!
        this.name1 = builder.name1!!
        this.name2 = builder.name2!!
        this.name3 = builder.name3!!
        this.savePath = builder.savePath!!
        this.typeface = if (builder.typeface == null) Typeface.DEFAULT_BOLD else builder.typeface!!
    }

    fun create(): File? {
        val paint = Paint()
        initBackgroundPaint(paint)

        val picture = Bitmap.createBitmap(backgroundWidth, backgroundHeight, Bitmap.Config.ARGB_8888)
        val background = Rect(0, 0, backgroundWidth, backgroundHeight)

        val canvas = Canvas(picture)
        canvas.drawRect(background, paint)

        val left1 = padding
        val left2 = left1 + pictureWidth + padding
        val left3 = left2 + pictureWidth + padding

        val ignoreSecond = path1 == path2
        val ignoreThird = path1 == path3

        val bitmap1 = getBitmapByFilePath(path1) ?: return null
        drawBitmap(canvas, bitmap1, left1, left1 + pictureWidth)

        if (ignoreSecond) {
            drawBitmap(canvas, bitmap1, left2, left2 + pictureWidth)
        }
        if (ignoreThird) {
            drawBitmap(canvas, bitmap1, left3, left3 + pictureWidth)
        }
        bitmap1.recycle()

        if (!ignoreSecond) {
            val bitmap2 = getBitmapByFilePath(path2) ?: return null
            drawBitmap(canvas, bitmap2, left2, left2 + pictureWidth)
            bitmap2.recycle()
        }

        if (!ignoreThird) {
            val bitmap3 = getBitmapByFilePath(path3) ?: return null
            drawBitmap(canvas, bitmap3, left3, left3 + pictureWidth)
            bitmap3.recycle()
        }

        initTextPaint(paint, typeface)

        val titleRect = Rect()
        paint.getTextBounds(title, 0, title.length, titleRect)
        val titleLeft = (backgroundWidth - titleRect.right) / 2f
        val titleTop = (textHeight - textSize) / 2f - titleRect.top
        canvas.drawText(title, titleLeft, titleTop, paint)

        drawText(canvas, paint, name1, left1)
        drawText(canvas, paint, name2, left2)
        drawText(canvas, paint, name3, left3)

        val imageName = "${System.currentTimeMillis()}.jpg"
        val newFile = ImageUtils.saveBitmapToJpg(picture, savePath, imageName)
        picture.recycle()

        return newFile
    }

    private fun initBackgroundPaint(paint: Paint) {
        paint.reset()
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
    }

    private fun initTextPaint(paint: Paint, typeface: Typeface) {
        paint.reset()
        paint.color = textColor
        paint.textSize = textSize.toFloat()
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.typeface = typeface
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap, left: Int, right: Int) {
        val pictureRect = Rect(0, 0, pictureWidth, pictureHeight)
        val dst = RectF(left.toFloat(), textHeight.toFloat(), right.toFloat(), (pictureHeight + textHeight).toFloat())
        canvas.drawBitmap(bitmap, pictureRect, dst, null)
    }

    private fun drawText(canvas: Canvas, paint: Paint, text: String, left: Int) {
        val nameRect = Rect()
        paint.getTextBounds(text, 0, text.length, nameRect)
        val nameTop = textHeight + pictureHeight + (textHeight - textSize) / 2f - nameRect.top
        val nameLeft = (pictureWidth - nameRect.right) / 2f + left
        canvas.drawText(text, nameLeft, nameTop, paint)
    }

    private fun getBitmapByFilePath(path: String): Bitmap? {
        val bitmap = BitmapFactory.decodeFile(path) ?: return null

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        if (bitmapWidth < pictureWidth || bitmapHeight < pictureHeight) {
            val scaleWidth = pictureWidth.toFloat() / bitmapWidth
            val scaleHeight = pictureHeight.toFloat() / bitmapHeight
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true)
            bitmap.recycle()
            return resizedBitmap
        }

        return bitmap
    }

    class Builder {
        var title: String? = null
        var path1: String? = null
        var path2: String? = null
        var path3: String? = null
        var name1: String? = null
        var name2: String? = null
        var name3: String? = null
        var savePath: String? = null
        var typeface: Typeface? = null

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun path1(path1: String): Builder {
            this.path1 = path1
            return this
        }

        fun path2(path2: String): Builder {
            this.path2 = path2
            return this
        }

        fun path3(path3: String): Builder {
            this.path3 = path3
            return this
        }

        fun name1(name1: String): Builder {
            this.name1 = name1
            return this
        }

        fun name2(name2: String): Builder {
            this.name2 = name2
            return this
        }

        fun name3(name3: String): Builder {
            this.name3 = name3
            return this
        }

        fun savePath(savePath: String): Builder {
            this.savePath = savePath
            return this
        }

        fun typeface(typeface: Typeface): Builder {
            this.typeface = typeface
            return this
        }

        fun build(): TripleSendHelper {
            return TripleSendHelper(this)
        }
    }

}