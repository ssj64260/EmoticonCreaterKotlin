package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory

class SecretEditActivity : BaseActivity() {

    companion object {
        private const val KEY_SECRET = "key_secret"

        fun show(activity: Activity, options: ActivityOptions, secret: PictureInfo) {
            val intent = Intent()
            intent.setClass(activity, SecretEditActivity().javaClass)
            intent.putExtra(KEY_SECRET, secret)
            activity.startActivity(intent, options.toBundle())
        }
    }

    private var ivPicture: AppCompatImageView? = null
    private var etTitle: AppCompatEditText? = null

    private var mSecret: PictureInfo? = null

    override fun getContentView(): Int {
        return R.layout.activity_secret_edit
    }

    override fun initData() {
        super.initData()
        mSecret = intent.getParcelableExtra(KEY_SECRET)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        setToolbarBackEnable()
        setToolbarTitle("添加秘密")
        setToolbarSubTitle("编写你的秘密")

        ivPicture = findViewById(R.id.iv_picture)
        etTitle = findViewById(R.id.et_title)

        if (mSecret != null) {
            val resourceId = mSecret!!.resourceId
            val title = mSecret!!.title

            ImageLoaderFactory.instance.loadImageFitCenter(this, ivPicture!!, resourceId, 0, 0)
            ivPicture!!.setImageResource(resourceId)
            etTitle!!.hint = title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (R.id.action_done == item!!.itemId) {
            doAdd()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_confirm_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun doAdd() {
        val title = etTitle!!.text!!.toString()
        if (TextUtils.isEmpty(title)) {
            showSnackbar("写下你的秘密")
        } else {
            hideKeyboard()
            mSecret!!.title = title

            TellTheSecretActivity.showOnNewIntent(this, mSecret!!)
        }
    }
}