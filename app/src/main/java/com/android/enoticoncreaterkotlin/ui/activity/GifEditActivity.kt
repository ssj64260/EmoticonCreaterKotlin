package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.model.GifText
import com.android.enoticoncreaterkotlin.model.GifTheme
import com.android.enoticoncreaterkotlin.util.FileUtils
import com.android.enoticoncreaterkotlin.util.GifHelper
import com.android.enoticoncreaterkotlin.util.SDCardUtils
import com.android.enoticoncreaterkotlin.util.ThreadPoolUtil
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.io.IOException
import java.util.*

class GifEditActivity : BaseActivity() {

    companion object {
        private const val KEY_GIF_THEME = "key_gif_theme"

        fun show(activity: Activity, theme: GifTheme) {
            val intent = Intent()
            intent.setClass(activity, GifEditActivity().javaClass)
            intent.putExtra(KEY_GIF_THEME, theme)
            activity.startActivity(intent)
        }
    }

    private var llContent: LinearLayout? = null
    private var ivGif: GifImageView? = null

    private var mTheme: GifTheme? = null
    private var mSavePath: String? = null
    private var mDrawable: GifDrawable? = null

    private var mTextList: ArrayList<GifText>? = null
    private var mEditTextList: ArrayList<EditText>? = null

    override fun getContentView(): Int {
        return R.layout.activity_gif_edit
    }

    override fun initData() {
        super.initData()
        mTheme = intent.getParcelableExtra(KEY_GIF_THEME)

        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_GIF
        FileUtils.createdirectory(mSavePath!!)

        mTextList = ArrayList()
        mEditTextList = ArrayList()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDrawable?.recycle()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_done) {
            doCreate()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_confirm_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarSubTitle("编辑对应的文字")

        llContent = findViewById(R.id.ll_content)
        ivGif = findViewById(R.id.iv_gif)

        if (mTheme != null) {
            val name = mTheme!!.name
            val fileName = mTheme!!.fileName
            val maxLength = mTheme!!.maxLength
            val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
            val textList = mTheme!!.textList

            setToolbarTitle(name!!)

            try {
                mDrawable = GifDrawable(assets, fileName!!)
                mDrawable!!.loopCount = 0
                ivGif!!.setImageDrawable(mDrawable)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (textList != null && !textList.isEmpty()) {
                mTextList!!.addAll(textList)

                for (i in mTextList!!.indices) {
                    val gifText = mTextList!![i]
                    val tips = "第" + (i + 1) + "句："
                    val hint = gifText.hint

                    val view = LayoutInflater.from(this).inflate(R.layout.layout_gif_text, null)
                    val tvTips = view.findViewById<TextView>(R.id.tv_tips)
                    val etText = view.findViewById<EditText>(R.id.et_text)

                    tvTips.text = tips
                    etText.hint = hint
                    etText.filters = filters
                    mEditTextList!!.add(etText)

                    llContent!!.addView(view)
                }
            }
        }
    }

    private fun doCreate() {
        var isAllEmpty = true

        for (i in mEditTextList!!.indices) {
            val editText = mEditTextList!![i]
            val gifText = mTextList!![i]
            val text = editText.text.toString()

            if (!TextUtils.isEmpty(text)) {
                gifText.text = text
                isAllEmpty = false
            }
        }

        for (i in mTextList!!.indices) {
            val gifText = mTextList!![i]
            val text = gifText.text

            if (isAllEmpty) {
                gifText.text = gifText.hint
            } else if (TextUtils.isEmpty(text)) {
                showSnackbar("请输入第" + (i + 1) + "句的内容")
                return
            }
        }

        hideKeyboard()
        showProgress("生成中...")

        ThreadPoolUtil.instance.cachedExecute(Runnable {
            val typeface = Typeface.createFromAsset(assets, "fonts/bold.ttf")
            val imageFile = GifHelper.create(assets, mTheme!!, mSavePath!!, typeface)

            runOnUiThread {
                if (imageFile != null && imageFile.exists()) {
                    val uri = Uri.fromFile(imageFile)
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                    showSnackbar("生成成功：" + imageFile.getAbsolutePath())
                } else {
                    showSnackbar("生成失败")
                }
                hideProgress()
            }
        })
    }
}