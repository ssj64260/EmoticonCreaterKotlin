package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.ui.adapter.EmoticonFragmentPagerAdapter
import com.android.enoticoncreaterkotlin.util.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import java.io.File

class OneEmoticonActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_SELECT_PICTURE = 100
        private const val REQUEST_CODE_CUTE_PICTURE = 101

        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, OneEmoticonActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var vpPicture: ViewPager? = null

    private var mCutePhotoFile: File? = null
    private var mTempPath: String? = null

    override fun getContentView(): Int {
        return R.layout.activity_one_emoticon
    }

    override fun initData() {
        super.initData()
        mTempPath = SDCardUtils.getExternalCacheDir(this)
        FileUtils.createdirectory(mTempPath!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("一个表情")

        vpPicture = findViewById(R.id.vp_picture)
        vpPicture!!.adapter = EmoticonFragmentPagerAdapter(supportFragmentManager, ImageDataHelper.EMOTICON_TITLES)

        setToolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
        setTabMode(TabLayout.MODE_SCROLLABLE)
        setupWithViewPager(vpPicture!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        val cacheDir = File(mTempPath)
        if (cacheDir.exists()) {
            DataCleanManager.deleteAllFiles(cacheDir)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        hideKeyboard()
        if (item!!.itemId == R.id.action_album) {
            doSelectPicture()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_one_emoticon, menu)
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

                    val picture = PictureInfo()
                    picture.filePath = filePath

                    OneEmoticonEditActivity.show(this, picture)
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
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
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
}