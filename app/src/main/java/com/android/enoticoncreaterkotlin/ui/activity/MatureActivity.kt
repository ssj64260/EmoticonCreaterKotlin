package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.util.*
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory
import java.io.File

class MatureActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_SELECT_PICTURE = 100
        private const val REQUEST_CODE_CUTE_PICTURE = 101

        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, MatureActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var ivPicture: AppCompatImageView? = null
    private var etTitle: AppCompatEditText? = null
    private var btnSelectSon: Button? = null

    private var mCutePhotoFile: File? = null
    private var mPictureFile: File? = null
    private var mTempPath: String? = null
    private var mSavePath: String? = null

    override fun getContentView(): Int {
        return R.layout.activity_mature
    }

    override fun initData() {
        super.initData()

        mTempPath = SDCardUtils.getExternalCacheDir(this)
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_MATURE

        FileUtils.createdirectory(mSavePath!!)
        FileUtils.createdirectory(mTempPath!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        setToolbarBackEnable()
        setToolbarTitle("你已经很成熟了")

        ivPicture = findViewById(R.id.iv_picture)
        etTitle = findViewById(R.id.et_title)
        btnSelectSon = findViewById(R.id.btn_select_child)

        btnSelectSon!!.setOnClickListener(mClick)
    }

    override fun onDestroy() {
        super.onDestroy()
        val cacheDir = File(mTempPath)
        if (cacheDir.exists()) {
            DataCleanManager.deleteAllFiles(cacheDir)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_done) {
            doCreate()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_confirm_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE_SELECT_PICTURE == requestCode) {
                if (data != null) {
                    val uri = data.data
                    if (uri != null) {
                        doCutPicture(uri)
                        return
                    }
                }
                showSnackbar("图片获取失败")
            } else if (REQUEST_CODE_CUTE_PICTURE == requestCode) {
                if (mCutePhotoFile != null && mCutePhotoFile!!.exists()) {
                    val filePath = mCutePhotoFile!!.absolutePath
                    doAddPicture(filePath)
                } else {
                    showSnackbar("图片裁剪失败")
                }
            }
        }
    }

    private fun doSelectPicture() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

        if (pickIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PICTURE)
        } else {
            showSnackbar("该系统没有选图工具")
        }
    }

    private fun doCutPicture(inputUri: Uri) {
        var uri = inputUri
        if (uri.toString().contains("file://")) {
            val path = uri.path
            val inputFile = File(path!!)
            uri = ImageUtils.getImageContentUri(this, inputFile)!!
        }

        mCutePhotoFile = File(mTempPath, System.currentTimeMillis().toString() + ".jpg")
        val outputUri = Uri.fromFile(mCutePhotoFile)

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", MatureHelper.HEADER_WIDTH)
        intent.putExtra("outputY", MatureHelper.HEADER_HEIGHT)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)

        if (intent.resolveActivity(packageManager) != null) {  //存在
            startActivityForResult(intent, REQUEST_CODE_CUTE_PICTURE)
        } else {
            showSnackbar("没有系统裁剪图片工具，本功能无法使用")
        }
    }

    private fun doAddPicture(filePath: String) {
        showProgress("图片处理中...")

        ThreadPoolUtil.instance.cachedExecute(Runnable {
            mPictureFile = MatureHelper.addPicture(resources, filePath, mTempPath!!)

            runOnUiThread {
                if (mPictureFile!!.exists()) {
                    val createFilePath = mPictureFile!!.absolutePath

                    ImageLoaderFactory.instance.loadImage(this, ivPicture!!, createFilePath)
                } else {
                    showSnackbar("生成失败，图片不存在")
                }
                hideProgress()
            }
        })
    }

    private fun doCreate() {
        val text = etTitle!!.text!!.toString()
        if (mPictureFile == null || !mPictureFile!!.exists()) {
            showSnackbar("图片不存在，请重新选择儿童头像")
        } else if (TextUtils.isEmpty(text)) {
            showSnackbar("请填写对儿童教导")
        } else {
            showProgress("图片处理中...")

            ThreadPoolUtil.instance.cachedExecute(Runnable {
                val pictureFilePath = mPictureFile!!.absolutePath
                val typeface = Typeface.createFromAsset(assets, "fonts/bold.ttf")
                val imageFile = MatureHelper.create(pictureFilePath, text, mSavePath!!, typeface)

                runOnUiThread {
                    if (imageFile.exists()) {
                        val filePath = imageFile.absolutePath

                        val uri = Uri.fromFile(imageFile)
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                        ShowPictureActivity.show(this@MatureActivity, filePath)
                    } else {
                        showSnackbar("生成失败，图片不存在")
                    }
                    hideProgress()
                }
            })
        }
    }

    private val mClick = View.OnClickListener { v ->
        when (v.id) {
            R.id.btn_select_child -> doSelectPicture()
        }
    }
}