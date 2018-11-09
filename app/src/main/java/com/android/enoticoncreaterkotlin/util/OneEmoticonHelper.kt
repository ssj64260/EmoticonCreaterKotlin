package com.android.enoticoncreaterkotlin.util

import android.content.res.Resources
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import com.android.enoticoncreaterkotlin.model.PictureInfo
import java.io.File

class OneEmoticonHelper private constructor(builder: Builder) {

    companion object {
        private const val TEXT_PADDING = 20//文字内边距
        private const val PICTURE_PADDING = 20//图片内边距
        private const val PICTURE_WIDTH = 300//图片宽度
        private const val BACKGROUND_COLOR = 0xffffffff.toInt()
        private const val TEXT_COLOR = 0xff010101.toInt()
    }

    private var mResources: Resources? = null
    private var mPicture: PictureInfo? = null
    private var mSavePath: String? = null
    private var mTypeFace: Typeface? = null
    private var mIsOriginal: Boolean? = null
    private var mTextSize: Int? = null

    init {
        mResources = builder.resources
        mPicture = builder.picture
        mSavePath = builder.savePath
        mTypeFace = builder.typeFace
        mIsOriginal = builder.isOriginal
        mTextSize = builder.textSize
    }

    fun create(): File {
        val quality = if (mIsOriginal!!) 100 else 5
        val config = if (mIsOriginal!!) Bitmap.Config.ARGB_8888 else Bitmap.Config.ARGB_4444

        val text = mPicture!!.title

        val resourceId = mPicture!!.resourceId
        val filePath = mPicture!!.filePath
        val bitmap: Bitmap
        if (!TextUtils.isEmpty(filePath)) {
            bitmap = getBitmapByFilePath(filePath!!)
        } else {
            bitmap = getBitmapByResourcesId(mResources!!, resourceId)
        }

        val pictureWidth = bitmap.width
        val pictureHeight = bitmap.height

        val textPaint = createTextPaint(mTypeFace!!)
        val currentLayout = StaticLayout(text, textPaint, pictureWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        val totalWidth = PICTURE_PADDING + pictureWidth + PICTURE_PADDING
        val totalHeight = PICTURE_PADDING + pictureHeight + TEXT_PADDING + currentLayout.height + TEXT_PADDING

        val background = Bitmap.createBitmap(totalWidth, totalHeight, config)
        val backgroundRect = Rect(0, 0, totalWidth, totalHeight)
        val canvas = Canvas(background)
        val backgroundPaint = createBackgroundPaint()
        canvas.drawRect(backgroundRect, backgroundPaint)

        val pictureRect = Rect(0, 0, pictureWidth, pictureHeight)
        val dst = RectF(PICTURE_PADDING.toFloat(), PICTURE_PADDING.toFloat(),
                (pictureWidth + PICTURE_PADDING).toFloat(), (PICTURE_PADDING + pictureHeight).toFloat())
        canvas.drawBitmap(bitmap, pictureRect, dst, null)
        bitmap.recycle()

        canvas.translate((totalWidth / 2).toFloat(), (PICTURE_PADDING + pictureHeight + TEXT_PADDING).toFloat())
        currentLayout.draw(canvas)

        val imageName = System.currentTimeMillis().toString() + ".jpg"
        val newFile = ImageUtils.saveBitmapToJpg(background, mSavePath!!, imageName, quality)
        background.recycle()

        return newFile
    }

    private fun createTextPaint(typeface: Typeface): TextPaint {
        val textPaint = TextPaint()
        textPaint.color = TEXT_COLOR
        textPaint.textSize = mTextSize!!.toFloat()
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

        if (bitmapWidth != PICTURE_WIDTH) {
            val scale = PICTURE_WIDTH.toFloat() / bitmapWidth

            val matrix = Matrix()
            matrix.postScale(scale, scale)
            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true)

            bitmap.recycle()

            return resizedBitmap
        }

        return bitmap
    }

    class Builder constructor(val resources: Resources) {
        var picture: PictureInfo? = null
        var savePath: String? = null
        var typeFace: Typeface? = null
        var isOriginal: Boolean? = null
        var textSize = 30

        fun pictureInfo(picture: PictureInfo): Builder {
            this.picture = picture
            return this
        }

        fun savePath(savePath: String): Builder {
            this.savePath = savePath
            return this
        }

        fun typeFace(typeFace: Typeface): Builder {
            this.typeFace = typeFace
            return this
        }

        fun isOriginal(isOriginal: Boolean): Builder {
            this.isOriginal = isOriginal
            return this
        }

        fun textSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        fun build(): OneEmoticonHelper {
            if (picture == null) {
                throw NullPointerException("PictureInfo is null")
            }
            if (TextUtils.isEmpty(savePath)) {
                throw NullPointerException("SavePath is empty")
            }
            if (typeFace == null) {
                throw NullPointerException("Typeface is null")
            }
            return OneEmoticonHelper(this)
        }
    }
}