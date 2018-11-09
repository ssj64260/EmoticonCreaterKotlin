package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.util.AllWickedHelper
import com.android.enoticoncreaterkotlin.util.FileUtils
import com.android.enoticoncreaterkotlin.util.SDCardUtils
import com.android.enoticoncreaterkotlin.util.ThreadPoolUtil

class AllWickedActivity : BaseActivity() {

    companion object {
        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, AllWickedActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var etDescritption: EditText? = null
    private var etAClothesText: EditText? = null
    private var etBClothesWord: EditText? = null
    private var etAAsk: EditText? = null
    private var etBReply: EditText? = null
    private var etBClothesText: EditText? = null

    private var mSavePath: String? = null

    override fun getContentView(): Int {
        return R.layout.activity_all_wicked
    }

    override fun initData() {
        super.initData()

        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_ALL_WICKED
        FileUtils.createdirectory(mSavePath!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        setToolbarBackEnable()
        setToolbarTitle("全员恶人")

        etDescritption = findViewById(R.id.et_descritption)
        etAClothesText = findViewById(R.id.et_a_clothes_text)
        etBClothesWord = findViewById(R.id.et_b_clothes_word)
        etAAsk = findViewById(R.id.et_a_ask)
        etBReply = findViewById(R.id.et_b_reply)
        etBClothesText = findViewById(R.id.et_b_clothes_text)
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

    private fun doCreate() {
        hideKeyboard()
        showProgress("图片处理中...")

        ThreadPoolUtil.instance.cachedExecute(Runnable {
            var description = etDescritption!!.text.toString()
            var aClothesText = etAClothesText!!.text.toString()
            var bClothesWord = etBClothesWord!!.text.toString()
            var aAsk = etAAsk!!.text.toString()
            var bReply = etBReply!!.text.toString()
            var bClothesText = etBClothesText!!.text.toString()

            if (TextUtils.isEmpty(description)) {
                description = etDescritption!!.hint.toString()
            }
            if (TextUtils.isEmpty(aClothesText)) {
                aClothesText = etAClothesText!!.hint.toString()
            }
            if (TextUtils.isEmpty(bClothesWord)) {
                bClothesWord = etBClothesWord!!.hint.toString()
            }
            if (TextUtils.isEmpty(aAsk)) {
                aAsk = etAAsk!!.hint.toString()
            }
            if (TextUtils.isEmpty(bReply)) {
                bReply = etBReply!!.hint.toString()
            }
            if (TextUtils.isEmpty(bClothesText)) {
                bClothesText = etBClothesText!!.hint.toString()
            }

            val typeface = Typeface.createFromAsset(assets, "fonts/bold.ttf")
            val helper = AllWickedHelper.Builder(resources)
                    .description(description)
                    .aClothesText(aClothesText)
                    .bClothesWord(bClothesWord)
                    .aAskText(aAsk)
                    .bReplyText(bReply)
                    .bClothesText(bClothesText)
                    .savePath(mSavePath!!)
                    .typeFace(typeface)
                    .build()

            val imageFile = helper.create()

            runOnUiThread {
                if (imageFile.exists()) {
                    val filePath = imageFile.absolutePath
                    val uri = Uri.fromFile(imageFile)

                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                    ShowPictureActivity.show(this@AllWickedActivity, filePath)
                } else {
                    showSnackbar("生成失败，图片不存在")
                }
                hideProgress()
            }
        })
    }
}