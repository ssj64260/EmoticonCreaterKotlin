package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SwitchCompat
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.util.FileUtils
import com.android.enoticoncreaterkotlin.util.OneEmoticonHelper
import com.android.enoticoncreaterkotlin.util.SDCardUtils
import com.android.enoticoncreaterkotlin.util.ThreadPoolUtil
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory

class OneEmoticonEditActivity : BaseActivity() {

    companion object {
        private const val KEY_ONE_EMOTICON = "key_one_emoticon"

        fun show(activity: Activity, picture: PictureInfo) {
            val intent = Intent()
            intent.setClass(activity, OneEmoticonEditActivity().javaClass)
            intent.putExtra(KEY_ONE_EMOTICON, picture)
            activity.startActivity(intent)
        }

        fun show(activity: Activity, options: ActivityOptions, picture: PictureInfo) {
            val intent = Intent()
            intent.setClass(activity, OneEmoticonEditActivity().javaClass)
            intent.putExtra(KEY_ONE_EMOTICON, picture)
            activity.startActivity(intent, options.toBundle())
        }
    }

    private var ivPicture: ImageView? = null
    private var etTitle: AppCompatEditText? = null
    private var tvQuality: TextView? = null
    private var swQuality: SwitchCompat? = null
    private var tvTextSize: TextView? = null
    private var sbTextSize: SeekBar? = null

    private var mPicture: PictureInfo? = null
    private var mSavePath: String? = null

    override fun getContentView(): Int {
        return R.layout.activity_one_emoticon_edit
    }

    override fun initData() {
        super.initData()
        mPicture = intent.getParcelableExtra(KEY_ONE_EMOTICON)

        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_ONE_EMOTICON
        FileUtils.createdirectory(mSavePath!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("编辑表情")
        setToolbarSubTitle("编写表情的文字")

        ivPicture = findViewById(R.id.iv_picture)
        etTitle = findViewById(R.id.et_title)
        tvQuality = findViewById(R.id.tv_quality)
        swQuality = findViewById(R.id.sw_quality)
        tvTextSize = findViewById(R.id.tv_text_size)
        sbTextSize = findViewById(R.id.sb_text_size)

        swQuality!!.setOnCheckedChangeListener { _, isChecked -> tvQuality!!.text = if (isChecked) "HD画质" else "AV画质" }
        swQuality!!.isChecked = true

        sbTextSize!!.setOnSeekBarChangeListener(mSeekBarChange)

        if (mPicture != null) {
            val filePath = mPicture!!.filePath
            val resourceId = mPicture!!.resourceId

            if (!TextUtils.isEmpty(filePath)) {
                ImageLoaderFactory.instance.loadImageFitCenter(this, ivPicture!!, filePath!!, 0, 0)
            } else {
                ImageLoaderFactory.instance.loadImageFitCenter(this, ivPicture!!, resourceId, 0, 0)
            }

            ivPicture!!.setImageResource(resourceId)
        }

        ivPicture!!.post { setTextSize(0) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (R.id.action_done == item!!.itemId) {
            doCreate()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_confirm_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setTextSize(progress: Int) {
        val pictureWidth = ivPicture!!.width
        val scale = pictureWidth / 300f
        val textSizePx = progress + 30
        val textSize = "字体:$textSizePx"
        tvTextSize!!.text = textSize
        etTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx * scale)
    }

    private fun doCreate() {
        if (mPicture == null) {
            showSnackbar("图片异常，请返回重新选图")
            return
        }

        hideKeyboard()
        showProgress("图片处理中...")

        val text = etTitle!!.text!!.toString()
        mPicture!!.title = text

        ThreadPoolUtil.instance.cachedExecute(Runnable {
            val isOriginal = swQuality!!.isChecked
            val typeface = Typeface.createFromAsset(assets, "fonts/bold.ttf")
            val textSizePx = sbTextSize!!.progress * 2 + 30

            val helper = OneEmoticonHelper.Builder(resources)
                    .pictureInfo(mPicture!!)
                    .savePath(mSavePath!!)
                    .typeFace(typeface)
                    .isOriginal(isOriginal)
                    .textSize(textSizePx)
                    .build()

            val imageFile = helper.create()

            runOnUiThread {
                if (imageFile.exists()) {
                    val filePath = imageFile.absolutePath

                    val uri = Uri.fromFile(imageFile)
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                    ShowPictureActivity.show(this@OneEmoticonEditActivity, filePath)
                } else {
                    showSnackbar("生成失败，图片不存在")
                }
                hideProgress()
            }
        })
    }

    private val mSeekBarChange = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val p = progress * 2
            setTextSize(p)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }
}