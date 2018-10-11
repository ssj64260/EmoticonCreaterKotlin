package com.android.enoticoncreaterkotlin.util

import android.content.res.Resources
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.android.enoticoncreaterkotlin.model.PictureInfo
import java.io.File

class SecretHelper {

    companion object {
        private val padding = 20//内边距
        private val pictureWidth = 500//图片宽度
        private val pictureHeight = 268//图片高度
        private val textSize = 40//字体大小
        private val backgroundColor = 0xffffffff.toInt()
        private val textColor = 0xff010101.toInt()

        fun createSecret(resources: Resources, secretList: List<PictureInfo>,
                         savePath: String, typeface: Typeface): File {
            val textPaint = createTextPaint(typeface)

            val totalWidth = padding + pictureWidth + padding
            var totalHeight = 0
            for (secret in secretList) {
                val text = secret.title
                val currentLayout = StaticLayout(text, textPaint, pictureWidth,
                        Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

                totalHeight += padding
                totalHeight += pictureHeight
                totalHeight += padding
                totalHeight += currentLayout.height
                totalHeight += padding
            }

            val backgroundPatnt = createBackgroundPaint()
            val picture = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
            val background = Rect(0, 0, totalWidth, totalHeight)
            val canvas = Canvas(picture)
            canvas.drawRect(background, backgroundPatnt)

            totalHeight = 0
            for (secret in secretList) {
                val text = secret.title
                val currentLayout = StaticLayout(text, textPaint, pictureWidth,
                        Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

                val resourceId = secret.resourceId

                totalHeight += padding

                canvas.translate(0f, padding.toFloat())
                drawBitmap(resources, canvas, resourceId)

                totalHeight += pictureHeight
                totalHeight += padding

                canvas.translate((totalWidth / 2).toFloat(), (pictureHeight + padding).toFloat())
                currentLayout.draw(canvas)

                totalHeight += currentLayout.height
                totalHeight += padding

                canvas.translate((-totalWidth / 2).toFloat(), (currentLayout.height + padding).toFloat())
            }

            val imageName = System.currentTimeMillis().toString() + ".jpg"
            val newFile = ImageUtils.saveBitmapToJpg(picture, savePath, imageName)
            picture.recycle()

            return newFile
        }

        private fun createBackgroundPaint(): Paint {
            val backgroundPaint = Paint()
            backgroundPaint.color = backgroundColor
            backgroundPaint.style = Paint.Style.FILL
            return backgroundPaint
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

        private fun drawBitmap(resources: Resources, canvas: Canvas, resourceId: Int) {
            val bitmap = getBitmapByResourcesId(resources, resourceId)
            val pictureRect = Rect(0, 0, pictureWidth, pictureHeight)
            val dst = RectF(padding.toFloat(), 0f, (pictureWidth + padding).toFloat(), pictureHeight.toFloat())
            canvas.drawBitmap(bitmap, pictureRect, dst, null)
            bitmap.recycle()
        }

        private fun getBitmapByResourcesId(resources: Resources, resourceId: Int): Bitmap {
            val bitmap = BitmapFactory.decodeResource(resources, resourceId)
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height

            if (bitmapWidth != pictureWidth || bitmapHeight != pictureHeight) {
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
    }

}