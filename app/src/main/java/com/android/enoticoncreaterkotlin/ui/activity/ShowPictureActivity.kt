package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.util.ImageUtils
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class ShowPictureActivity : BaseActivity() {

    companion object {
        private const val KEY_PICTURE_FILE_PATH = "key_picture_file_path"

        fun show(activity: Activity, filePath: String) {
            val intent = Intent()
            intent.setClass(activity, ShowPictureActivity().javaClass)
            intent.putExtra(KEY_PICTURE_FILE_PATH, filePath)
            activity.startActivity(intent)
        }
    }

    private var ivPicture: ImageView? = null
    private var btnSend: FloatingActionButton? = null

    private var mPictureFile: File? = null

    override fun getContentView(): Int {
        return R.layout.activity_show_picture
    }

    override fun initData() {
        super.initData()
        val filePath = intent.getStringExtra(KEY_PICTURE_FILE_PATH)
        if (!TextUtils.isEmpty(filePath)) {
            mPictureFile = File(filePath)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("图片预览")

        ivPicture = findViewById(R.id.iv_picture)
        btnSend = findViewById(R.id.btn_send)

        if (mPictureFile != null && mPictureFile!!.exists()) {
            ImageLoaderFactory.instance.loadImageFitCenter(this, ivPicture!!, mPictureFile!!, 0, 0)
            showSnackbar("保存路径：" + mPictureFile!!.absolutePath)
        } else {
            showSnackbar("图片生成失败")
        }

        btnSend!!.setOnClickListener(mClick)
    }

    private fun doSend() {
        if (mPictureFile != null && mPictureFile!!.exists() && mPictureFile!!.isFile()) {
            val uri = ImageUtils.getUriFromFile(this, mPictureFile!!)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent.createChooser(intent, ""))
        } else {
            showSnackbar("图片不存在")
        }
    }

    private val mClick = View.OnClickListener { v ->
        when (v.id) {
            R.id.btn_send -> doSend()
        }
    }
}