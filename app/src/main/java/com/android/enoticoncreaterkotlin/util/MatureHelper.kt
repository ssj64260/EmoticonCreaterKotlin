package com.android.enoticoncreaterkotlin.util

import android.content.res.Resources
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.android.enoticoncreaterkotlin.R
import java.io.File

class MatureHelper {

    companion object {
        const val HEADER_WIDTH = 184
        const val HEADER_HEIGHT = 184

        private const val PICTURE_WIDTH = 700
        private const val PICTURE_HEIGHT = 500
        private const val PADDING = 20
        private const val MARGIN_TOP = 270
        private const val MARGIN_LEFT = 114
        private const val TEXT_SIZE = 44//字体大小
        private const val TEXT_COLOR = 0xff010101.toInt()
        private const val BACKGROUND_COLOR = 0xffffffff.toInt()

        fun addPicture(resources: Resources, childFilePath: String, savePath: String): File {
            val background = Bitmap.createBitmap(PICTURE_WIDTH, PICTURE_HEIGHT, Bitmap.Config.ARGB_8888)
            val backgroundRect = Rect(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT)
            val canvas = Canvas(background)
            val backgroundPaint = createBackgroundPaint()
            canvas.drawRect(backgroundRect, backgroundPaint)

            val childBitmap = getBitmapByFilePath(childFilePath, HEADER_WIDTH)
            val childWidth = childBitmap.width
            val childHeight = childBitmap.height
            val childRect = Rect(0, 0, childWidth, childHeight)
            val childRectF = RectF(MARGIN_LEFT.toFloat(), MARGIN_TOP.toFloat(),
                    (MARGIN_LEFT + childWidth).toFloat(), (MARGIN_TOP + childHeight).toFloat())
            canvas.drawBitmap(childBitmap, childRect, childRectF, null)
            childBitmap.recycle()

            val parentBitmap = getBitmapByResourcesId(resources, R.raw.img_mature)
            val parentRect = Rect(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT)
            val parentRectF = RectF(0f, 0f, PICTURE_WIDTH.toFloat(), PICTURE_HEIGHT.toFloat())
            canvas.drawBitmap(parentBitmap, parentRect, parentRectF, null)
            parentBitmap.recycle()

            val imageName = System.currentTimeMillis().toString() + ".jpg"
            val newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, 100)
            background.recycle()

            return newFile
        }

        fun create(pictureFilePath: String, text: String, savePath: String, typeface: Typeface): File {
            val picture = getBitmapByFilePath(pictureFilePath, PICTURE_WIDTH)
            val pictureWidth = picture.width
            val pictureHeight = picture.height

            val textPaint = createTextPaint(typeface)
            val currentLayout = StaticLayout(text, textPaint, pictureWidth - PADDING * 2,
                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

            val totalHeight = pictureHeight + PADDING + currentLayout.height + PADDING

            val background = Bitmap.createBitmap(pictureWidth, totalHeight, Bitmap.Config.ARGB_8888)
            val backgroundRect = Rect(0, 0, pictureWidth, totalHeight)
            val canvas = Canvas(background)
            val backgroundPaint = createBackgroundPaint()
            canvas.drawRect(backgroundRect, backgroundPaint)

            val pictureRect = Rect(0, 0, pictureWidth, pictureHeight)
            val dst = RectF(0f, 0f, pictureWidth.toFloat(), pictureHeight.toFloat())
            canvas.drawBitmap(picture, pictureRect, dst, null)
            picture.recycle()

            canvas.translate(pictureWidth / 2f, (pictureHeight + PADDING).toFloat())
            currentLayout.draw(canvas)

            val imageName = "${System.currentTimeMillis()}.jpg"
            val newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, 100)
            background.recycle()

            return newFile
        }

        private fun createTextPaint(typeface: Typeface): TextPaint {
            val textPaint = TextPaint()
            textPaint.color = TEXT_COLOR
            textPaint.textSize = TEXT_SIZE.toFloat()
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.flags = Paint.ANTI_ALIAS_FLAG
            textPaint.typeface = typeface
            return textPaint
        }

        private fun createBackgroundPaint(): Paint {
            val backgroundPaint = Paint()
            backgroundPaint.color = BACKGROUND_COLOR
            backgroundPaint.style = Paint.Style.FILL
            return backgroundPaint
        }

        private fun getBitmapByFilePath(filePath: String, width: Int): Bitmap {
            val bitmap = BitmapFactory.decodeFile(filePath)
            return setScale(bitmap, width)
        }

        private fun getBitmapByResourcesId(resources: Resources, resourceId: Int): Bitmap {
            val bitmap = BitmapFactory.decodeResource(resources, resourceId)
            return setScale(bitmap, PICTURE_WIDTH)
        }

        private fun setScale(bitmap: Bitmap, totalWidth: Int): Bitmap {
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height

            if (bitmapWidth != totalWidth) {
                val scale = totalWidth.toFloat() / bitmapWidth

                val matrix = Matrix()
                matrix.postScale(scale, scale)
                val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true)
                bitmap.recycle()

                return resizedBitmap
            }

            return bitmap
        }
    }

}