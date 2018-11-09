package com.android.enoticoncreaterkotlin.util

import android.content.res.Resources
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.android.enoticoncreaterkotlin.R
import java.io.File

class AllWickedHelper private constructor(builder: Builder) {

    companion object {
        private const val PICTURE_WIDTH = 500

        private const val FIRST_CLOTHES_TEXT_CENTER = 258
        private const val FIRST_CLOTHES_TEXT_TOP = 99
        private const val FIRST_PICUTRE = R.raw.img_all_wicked1
        private const val FIRST_PICUTRE_HEIGHT = 318

        private const val SECOND_CLOTHES_TEXT_CENTER = 116
        private const val SECOND_CLOTHES_TEXT_TOP = 130
        private const val SECOND_PICUTRE = R.raw.img_all_wicked2
        private const val SECOND_PICUTRE_HEIGHT = 446

        private const val THIRD_CLOTHES_TEXT_CENTER = 451
        private const val THIRD_CLOTHES_TEXT_TOP = 206
        private const val THIRD_PICUTRE = R.raw.img_all_wicked3
        private const val THIRD_PICUTRE_HEIGHT = 384

        private const val FORTH_CLOTHES_TEXT_CENTER = 260
        private const val FORTH_CLOTHES_TEXT_TOP = 100
        private const val FORTH_PICUTRE = R.raw.img_all_wicked4
        private const val FORTH_PICUTRE_HEIGHT = 285

        private const val DESCRIPTION_TEXT_SIZE = 40
        private const val CLOTHES_BIG_TEXT_SIZE = 36
        private const val CLOTHES_BIG_TEXT_WIDTH = 144
        private const val CLOTHES_TEXT_WIDTH = 72
        private const val CLOTHES_WORD_WIDTH = 18
        private const val CLOTHES_TEXT_SIZE = 18

        private const val PADDING = 40
        private const val TEXT_PADDING = 20
        private const val TEXT_COLOR = 0xff010101.toInt()
        private const val CLOTHES_TEXT_COLOR = 0xffffffff.toInt()
        private const val BACKGROUND_COLOR = 0xffffffff.toInt()
    }

    private val mResources: Resources? = null
    private val mDescription: String? = null//描述
    private val mAClothesText: String? = null//A的衣服文字
    private val mBClothesText: String? = null//B的衣服文字
    private val mBClothesWord: String? = null//B的衣服文字的第一个或最后一个字
    private val mAAskText: String? = null//A的提问
    private val mBReplyText: String? = null//B的回答
    private val mSavePath: String? = null
    private val mTypeFace: Typeface? = null

    fun create(): File {
        val textPaint = createTextPaint(mTypeFace!!, DESCRIPTION_TEXT_SIZE, TEXT_COLOR)
        val textWidth = PICTURE_WIDTH - TEXT_PADDING * 2
        val descriptionLayout = StaticLayout(mDescription, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        val aAskLayout = StaticLayout(mAAskText, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        val bReplyLayout = StaticLayout(mBReplyText, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        val totalHeight = PADDING + descriptionLayout.height + PADDING
        +FIRST_PICUTRE_HEIGHT + PADDING + SECOND_PICUTRE_HEIGHT
        +PADDING + aAskLayout.height
        +THIRD_PICUTRE_HEIGHT + PADDING
        +bReplyLayout.height
        +FORTH_PICUTRE_HEIGHT + PADDING

        val background = Bitmap.createBitmap(PICTURE_WIDTH, totalHeight, Bitmap.Config.ARGB_8888)
        val backgroundRect = Rect(0, 0, PICTURE_WIDTH, totalHeight)
        val canvas = Canvas(background)
        val backgroundPaint = createBackgroundPaint()
        canvas.drawRect(backgroundRect, backgroundPaint)

        val clothesTextPaint = createTextPaint(mTypeFace, CLOTHES_TEXT_SIZE, CLOTHES_TEXT_COLOR)

        drawFirstPart(canvas, descriptionLayout, clothesTextPaint)
        drawSecondPart(canvas, clothesTextPaint)
        drawThirdPart(canvas, aAskLayout)
        drawForthPart(canvas, bReplyLayout, clothesTextPaint)

        val imageName = "${System.currentTimeMillis()}.jpg"
        val newFile = ImageUtils.saveBitmapToJpg(background, mSavePath!!, imageName, 100)
        background.recycle()

        return newFile
    }

    private fun drawFirstPart(canvas: Canvas, descriptionLayout: StaticLayout, clothesTextPaint: TextPaint) {
        canvas.translate((PICTURE_WIDTH / 2).toFloat(), PADDING.toFloat())
        descriptionLayout.draw(canvas)

        canvas.translate((-PICTURE_WIDTH / 2).toFloat(), (descriptionLayout.height + PADDING).toFloat())
        drawBitmap(mResources!!, canvas, FIRST_PICUTRE)

        val clothesLayout = StaticLayout(mAClothesText, clothesTextPaint, CLOTHES_TEXT_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        canvas.translate(FIRST_CLOTHES_TEXT_CENTER.toFloat(), FIRST_CLOTHES_TEXT_TOP.toFloat())
        clothesLayout.draw(canvas)
        canvas.translate((-FIRST_CLOTHES_TEXT_CENTER).toFloat(), (-FIRST_CLOTHES_TEXT_TOP).toFloat())
    }

    private fun drawSecondPart(canvas: Canvas, clothesTextPaint: TextPaint) {
        canvas.translate(0f, (FIRST_PICUTRE_HEIGHT + PADDING).toFloat())
        drawBitmap(mResources!!, canvas, SECOND_PICUTRE)

        val clothesWordLayout = StaticLayout(mBClothesWord, clothesTextPaint, CLOTHES_WORD_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        canvas.translate(SECOND_CLOTHES_TEXT_CENTER.toFloat(), SECOND_CLOTHES_TEXT_TOP.toFloat())
        clothesWordLayout.draw(canvas)
        canvas.translate((-SECOND_CLOTHES_TEXT_CENTER).toFloat(), (-SECOND_CLOTHES_TEXT_TOP).toFloat())
    }

    private fun drawThirdPart(canvas: Canvas, aAskLayout: StaticLayout) {
        canvas.translate((PICTURE_WIDTH / 2).toFloat(), (SECOND_PICUTRE_HEIGHT + PADDING).toFloat())
        aAskLayout.draw(canvas)

        canvas.translate((-PICTURE_WIDTH / 2).toFloat(), aAskLayout.height.toFloat())
        drawBitmap(mResources!!, canvas, THIRD_PICUTRE)

        val clothesBigTextPaint = createTextPaint(mTypeFace!!, CLOTHES_BIG_TEXT_SIZE, CLOTHES_TEXT_COLOR)
        val clothesLayout = StaticLayout(mAClothesText, clothesBigTextPaint, CLOTHES_BIG_TEXT_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        canvas.translate(THIRD_CLOTHES_TEXT_CENTER.toFloat(), THIRD_CLOTHES_TEXT_TOP.toFloat())
        clothesLayout.draw(canvas)
        canvas.translate((-THIRD_CLOTHES_TEXT_CENTER).toFloat(), (-THIRD_CLOTHES_TEXT_TOP).toFloat())
    }

    private fun drawForthPart(canvas: Canvas, bReplyLayout: StaticLayout, clothesTextPaint: TextPaint) {
        canvas.translate((PICTURE_WIDTH / 2).toFloat(), (THIRD_PICUTRE_HEIGHT + PADDING).toFloat())
        bReplyLayout.draw(canvas)

        canvas.translate((-PICTURE_WIDTH / 2).toFloat(), bReplyLayout.height.toFloat())
        drawBitmap(mResources!!, canvas, FORTH_PICUTRE)

        val bClothesLayout = StaticLayout(mBClothesText, clothesTextPaint, CLOTHES_TEXT_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        canvas.translate(FORTH_CLOTHES_TEXT_CENTER.toFloat(), FORTH_CLOTHES_TEXT_TOP.toFloat())
        bClothesLayout.draw(canvas)
    }

    private fun drawBitmap(resources: Resources, canvas: Canvas, resourceId: Int) {
        val bitmap = getBitmapByResourcesId(resources, resourceId)
        val width = bitmap.width
        val height = bitmap.height

        val pictureRect = Rect(0, 0, width, height)
        val dst = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawBitmap(bitmap, pictureRect, dst, null)
        bitmap.recycle()
    }

    private fun createTextPaint(typeface: Typeface, textSize: Int, textColor: Int): TextPaint {
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
        backgroundPaint.color = BACKGROUND_COLOR
        backgroundPaint.style = Paint.Style.FILL
        return backgroundPaint
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

    class Builder constructor(resources: Resources) {
        var description: String? = null
        var aClothesText: String? = null
        var bClothesText: String? = null
        var bClothesWord: String? = null
        var aAskText: String? = null
        var bReplyText: String? = null
        var savePath: String? = null
        var typeFace: Typeface? = null

        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun aClothesText(aClothesText: String): Builder {
            this.aClothesText = aClothesText
            return this
        }

        fun bClothesText(bClothesText: String): Builder {
            this.bClothesText = bClothesText
            return this
        }

        fun bClothesWord(bClothesWord: String): Builder {
            this.bClothesWord = bClothesWord
            return this
        }

        fun aAskText(aAskText: String): Builder {
            this.aAskText = aAskText
            return this
        }

        fun bReplyText(bReplyText: String): Builder {
            this.bReplyText = bReplyText
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

        fun build(): AllWickedHelper {
            return AllWickedHelper(this)
        }
    }
}