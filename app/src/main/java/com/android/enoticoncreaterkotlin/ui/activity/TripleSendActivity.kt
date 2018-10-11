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
import android.widget.EditText
import android.widget.ImageView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.db.LiteOrmHelper
import com.android.enoticoncreaterkotlin.model.ThreeProverbInfo
import com.android.enoticoncreaterkotlin.util.*
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory
import java.io.File

class TripleSendActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_PICTURE1 = 1
        private const val REQUEST_CODE_PICTURE2 = 1 shl 1
        private const val REQUEST_CODE_PICTURE3 = 1 shl 2
        private const val REQUEST_CODE_SELECT_PICTURE = 1 shl 4
        private const val REQUEST_CODE_CUTE_PICTURE = 1 shl 5

        private const val REQUEST_CODE_TO_PROVERB = 1004

        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, TripleSendActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var etTitle: EditText? = null
    private var ivPicture1: ImageView? = null
    private var ivPicture2: ImageView? = null
    private var ivPicture3: ImageView? = null
    private var etName1: EditText? = null
    private var etName2: EditText? = null
    private var etName3: EditText? = null
    private var btnDoCreate: Button? = null
    private var ivPreview: ImageView? = null
    private var btnDoSave: Button? = null
    private var btnDoSend: Button? = null

    private var mPath1: String? = null
    private var mPath2: String? = null
    private var mPath3: String? = null
    private var mSavePath: String? = null
    private var mTempPath: String? = null
    private var mCutePhotoFile: File? = null
    private var mCurrentImage: File? = null
    private var mDBHelper: LiteOrmHelper? = null

    override fun getContentView(): Int {
        return R.layout.activity_triple_send
    }

    override fun initData() {
        super.initData()
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_TRIPLE_SEND
        mTempPath = SDCardUtils.getExternalCacheDir(this)
        FileUtils.createdirectory(mSavePath!!)
        FileUtils.createdirectory(mTempPath!!)

        mDBHelper = LiteOrmHelper(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("表情三连发")

        etTitle = findViewById(R.id.et_title)
        ivPicture1 = findViewById(R.id.iv_picture1)
        ivPicture2 = findViewById(R.id.iv_picture2)
        ivPicture3 = findViewById(R.id.iv_picture3)
        etName1 = findViewById(R.id.et_name1)
        etName2 = findViewById(R.id.et_name2)
        etName3 = findViewById(R.id.et_name3)
        btnDoCreate = findViewById(R.id.btn_do_create)
        ivPreview = findViewById(R.id.iv_preview)
        btnDoSave = findViewById(R.id.btn_do_save)
        btnDoSend = findViewById(R.id.btn_do_send)

        ivPicture1!!.setOnClickListener(mClick)
        ivPicture2!!.setOnClickListener(mClick)
        ivPicture3!!.setOnClickListener(mClick)
        btnDoCreate!!.setOnClickListener(mClick)
        btnDoSave!!.setOnClickListener(mClick)
        btnDoSend!!.setOnClickListener(mClick)
    }

    override fun onDestroy() {
        super.onDestroy()
        val cacheDir = File(mTempPath)
        if (cacheDir.exists()) {
            DataCleanManager.deleteAllFiles(cacheDir)
        }
        mDBHelper?.closeDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE_TO_PROVERB == requestCode) {
                val proverb: ThreeProverbInfo? = data!!.getParcelableExtra(Constants.KEY_RETURN_DATA)
                if (proverb != null) {
                    etTitle!!.setText(proverb.title)
                    etName1!!.setText(proverb.firstProverb)
                    etName2!!.setText(proverb.secondProverb)
                    etName3!!.setText(proverb.thirdProverb)
                }
            } else {
                val actionCode = getActionCode(requestCode)
                val pictureCode = getPictureCode(requestCode)
                if (actionCode == REQUEST_CODE_SELECT_PICTURE) {
                    if (data != null) {
                        val uri = data.data
                        if (uri != null) {
                            doCutPicture(uri, pictureCode)
                            return
                        }
                    }
                    showSnackbar("图片获取失败")
                } else if (actionCode == REQUEST_CODE_CUTE_PICTURE) {
                    if (mCutePhotoFile != null && mCutePhotoFile!!.exists()) {
                        when {
                            REQUEST_CODE_PICTURE1 == pictureCode -> {
                                mPath1 = mCutePhotoFile!!.absolutePath
                                mPath2 = mPath1
                                mPath3 = mPath1
                                ImageLoaderFactory.instance.loadImage(this, ivPicture1!!, mPath1!!)
                                ImageLoaderFactory.instance.loadImage(this, ivPicture2!!, mPath2!!)
                                ImageLoaderFactory.instance.loadImage(this, ivPicture3!!, mPath3!!)
                            }
                            REQUEST_CODE_PICTURE2 == pictureCode -> {
                                mPath2 = mCutePhotoFile!!.absolutePath
                                ImageLoaderFactory.instance.loadImage(this, ivPicture2!!, mPath2!!)
                            }
                            else -> {
                                mPath3 = mCutePhotoFile!!.absolutePath
                                ImageLoaderFactory.instance.loadImage(this, ivPicture3!!, mPath3!!)
                            }
                        }
                    } else {
                        showSnackbar("图片裁剪失败")
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        hideKeyboard()
        if (item!!.itemId == R.id.action_proverb) {
            ThreeProverbListActivity.show(this, REQUEST_CODE_TO_PROVERB)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tools, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun doSelectPicture(requestCode: Int) {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

        if (pickIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(pickIntent, requestCode or REQUEST_CODE_SELECT_PICTURE)
        } else {
            showSnackbar("该系统没有选图工具")
        }
    }

    private fun doCutPicture(inputUri: Uri, requestCode: Int) {
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
        intent.putExtra("outputX", 200)
        intent.putExtra("outputY", 200)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, requestCode or REQUEST_CODE_CUTE_PICTURE)
        } else {
            showSnackbar("没有系统裁剪图片工具，本功能无法使用")
        }
    }

    private fun doCreatePicture() {
        val title = etTitle!!.text.toString()
        val name1 = etName1!!.text.toString()
        val name2 = etName2!!.text.toString()
        val name3 = etName3!!.text.toString()
        when {
            TextUtils.isEmpty(title) -> showSnackbar("请先输入标题")
            TextUtils.isEmpty(mPath1) -> showSnackbar("请先选择图片1")
            TextUtils.isEmpty(mPath2) -> showSnackbar("请先选择图片2")
            TextUtils.isEmpty(mPath3) -> showSnackbar("请先选择图片3")
            TextUtils.isEmpty(name1) -> showSnackbar("请输入图片1文字内容")
            TextUtils.isEmpty(name2) -> showSnackbar("请输入图片2文字内容")
            TextUtils.isEmpty(name3) -> showSnackbar("请输入图片3文字内容")
            else -> {
                showProgress("图片处理中...")
                ThreadPoolUtil.instance.cachedExecute(Runnable {
                    val typeface = Typeface.createFromAsset(assets, "fonts/bold.ttf")

                    val helper = TripleSendHelper.Builder()
                            .title(title)
                            .path1(mPath1!!)
                            .path2(mPath2!!)
                            .path3(mPath3!!)
                            .name1(name1)
                            .name2(name2)
                            .name3(name3)
                            .savePath(mSavePath!!)
                            .typeface(typeface)
                            .build()

                    mCurrentImage = helper.create()

                    doStatistics(title, name1, name2, name3)

                    runOnUiThread {
                        when {
                            mCurrentImage == null -> showSnackbar("已选择的图片不存在，请重新选择")
                            mCurrentImage!!.exists() -> {
                                val filePath = mCurrentImage!!.absolutePath
                                ImageLoaderFactory.instance.loadImage(this@TripleSendActivity, ivPreview!!, filePath)

                                refreshAlbum(mCurrentImage)

                                showSnackbar("保存路径：$filePath")
                            }
                            else -> showSnackbar("图片生成失败")
                        }
                        hideProgress()
                    }
                })
            }
        }
    }

    private fun refreshAlbum(file: File?) {
        if (file != null && file.exists()) {
            val uri = Uri.fromFile(file)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        }
    }

    private fun doStatistics(title: String, name1: String, name2: String, name3: String) {
        val proverb = mDBHelper!!.queryFirst(ThreeProverbInfo().javaClass,
                "title == ? and firstProverb == ? and secondProverb == ? and thirdProverb == ?",
                title, name1, name2, name3)
        if (proverb != null) {
            proverb.useTimes = proverb.useTimes + 1
            mDBHelper!!.update(proverb)
        }
    }

    private fun doSaveProverb() {
        val title = etTitle!!.text.toString()
        val name1 = etName1!!.text.toString()
        val name2 = etName2!!.text.toString()
        val name3 = etName3!!.text.toString()
        if (TextUtils.isEmpty(title)) {
            showSnackbar("请先输入标题")
        } else if (TextUtils.isEmpty(name1)) {
            showSnackbar("请输入图片1文字内容")
        } else if (TextUtils.isEmpty(name2)) {
            showSnackbar("请输入图片2文字内容")
        } else if (TextUtils.isEmpty(name3)) {
            showSnackbar("请输入图片3文字内容")
        } else {
            var proverb = mDBHelper!!.queryFirst(ThreeProverbInfo().javaClass,
                    "title == ? and firstProverb == ? and secondProverb == ? and thirdProverb == ?",
                    arrayOf(title, name1, name2, name3))
            if (proverb == null) {
                proverb = ThreeProverbInfo()
                proverb.title = title
                proverb.firstProverb = name1
                proverb.secondProverb = name2
                proverb.thirdProverb = name3
                proverb.useTimes = 1
                mDBHelper!!.save(proverb)
                showSnackbar("保存成功")
            } else {
                showSnackbar("这套语录已在“怼人语录”里")
            }
        }
    }

    private fun doSend() {
        if (mCurrentImage != null && mCurrentImage!!.exists() && mCurrentImage!!.isFile) {
            val uri = ImageUtils.getUriFromFile(this, mCurrentImage!!)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent.createChooser(intent, ""))
        } else {
            ivPreview!!.setImageResource(0)
            showSnackbar("图片不存在")
        }
    }

    private fun getPictureCode(code: Int): Int {
        return when {
            REQUEST_CODE_PICTURE1 and code != 0 -> REQUEST_CODE_PICTURE1
            REQUEST_CODE_PICTURE2 and code != 0 -> REQUEST_CODE_PICTURE2
            REQUEST_CODE_PICTURE3 and code != 0 -> REQUEST_CODE_PICTURE3
            else -> -1
        }

    }

    private fun getActionCode(code: Int): Int {
        if (REQUEST_CODE_SELECT_PICTURE and code != 0) {
            return REQUEST_CODE_SELECT_PICTURE
        } else if (REQUEST_CODE_CUTE_PICTURE and code != 0) {
            return REQUEST_CODE_CUTE_PICTURE
        }

        return -1
    }

    private val mClick = View.OnClickListener { v ->
        hideKeyboard()
        when (v.id) {
            R.id.iv_picture1 -> doSelectPicture(REQUEST_CODE_PICTURE1)
            R.id.iv_picture2 -> doSelectPicture(REQUEST_CODE_PICTURE2)
            R.id.iv_picture3 -> doSelectPicture(REQUEST_CODE_PICTURE3)
            R.id.btn_do_create -> doCreatePicture()
            R.id.btn_do_save -> doSaveProverb()
            R.id.btn_do_send -> doSend()
        }
    }
}