package com.android.enoticoncreaterkotlin.util

import android.content.res.Resources
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import com.android.enoticoncreaterkotlin.model.PictureInfo
import java.io.File

class OneEmoticonHelper {

    companion object {
        private const val padding = 20//内边距
        private const val pictureWidth = 300//图片宽度
        private const val textSize = 30//字体大小
        private const val backgroundColor = 0xffffffff.toInt()
        private const val textColor = 0xff010101.toInt()

        fun create(resources: Resources, emoticon: PictureInfo,
                   savePath: String, typeface: Typeface, isOriginal: Boolean): File {
            val quality = if (isOriginal) 100 else 5
            val config = if (isOriginal) Bitmap.Config.ARGB_8888 else Bitmap.Config.ARGB_4444

            val text = emoticon.title

            val resourceId = emoticon.resourceId
            val filePath = emoticon.filePath
            val bitmap: Bitmap
            if (!TextUtils.isEmpty(filePath)) {
                bitmap = getBitmapByFilePath(filePath!!)
            } else {
                bitmap = getBitmapByResourcesId(resources, resourceId)
            }

            val pictureWidth = bitmap.width
            val pictureHeight = bitmap.height

            val textPaint = createTextPaint(typeface)
            val currentLayout = StaticLayout(text, textPaint, pictureWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

            val totalWidth = padding + pictureWidth + padding
            val totalHeight = padding + pictureHeight + padding + currentLayout.height + padding

            val background = Bitmap.createBitmap(totalWidth, totalHeight, config)
            val backgroundRect = Rect(0, 0, totalWidth, totalHeight)
            val canvas = Canvas(background)
            val backgroundPaint = createBackgroundPaint()
            canvas.drawRect(backgroundRect, backgroundPaint)

            val pictureRect = Rect(0, 0, pictureWidth, pictureHeight)
            val dst = RectF(padding.toFloat(), padding.toFloat(), (pictureWidth + padding).toFloat(), (padding + pictureHeight).toFloat())
            canvas.drawBitmap(bitmap, pictureRect, dst, null)
            bitmap.recycle()

            canvas.translate((totalWidth / 2).toFloat(), (padding + pictureHeight + padding).toFloat())
            currentLayout.draw(canvas)

            val imageName = System.currentTimeMillis().toString() + ".jpg"
            val newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, quality)
            background.recycle()

            return newFile
        }

        private fun createTextPaint(typeface: Typeface): TextPaint {
            val textPaint = TextPaint()
            textPaint.color = textColor
            textPaint.textSize = textSize.toFloat()
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.flags = Paint.ANTI_ALIAS_FLAG
            textPaint.typeface = typeface
            return textPaint
        }

        private fun createBackgroundPaint(): Paint {
            val backgroundPaint = Paint()
            backgroundPaint.color = backgroundColor
            backgroundPaint.style = Paint.Style.FILL
            return backgroundPaint
        }

        private fun getBitmapByFilePath(filePath: String): Bitmap {
            val bitmap = BitmapFactory.decodeFile(filePath)
            return setScale(bitmap)
        }

        private fun getBitmapByResourcesId(resources: Resources, resourceId: Int): Bitmap {
            val bitmap = BitmapFactory.decodeResource(resources, resourceId)
            return setScale(bitmap)
        }

        private fun setScale(bitmap: Bitmap): Bitmap {
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height

            if (bitmapWidth != pictureWidth) {
                val scale = pictureWidth.toFloat() / bitmapWidth

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