package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.model.GifTheme
import com.android.enoticoncreaterkotlin.ui.adapter.GifThemeListAdapter
import com.android.enoticoncreaterkotlin.ui.adapter.OnListClickListener
import com.android.enoticoncreaterkotlin.util.AssetsUtil
import java.util.*

class GifThemeListActivity : BaseActivity() {

    companion object {
        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, GifThemeListActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var rvThemeList: RecyclerView? = null

    private var mThemeList: ArrayList<GifTheme>? = null
    private var mThemeAdapter: GifThemeListAdapter? = null

    override fun getContentView(): Int {
        return R.layout.activity_gif_theme_list
    }

    override fun initData() {
        super.initData()
        mThemeList = ArrayList()
        val jsonText = AssetsUtil.getAssetsTxtByName(this, "Gif_Theme.json")
        if (!TextUtils.isEmpty(jsonText)) {
            val themeList: List<GifTheme> = JSON.parseArray(jsonText, GifTheme().javaClass)
            if (!themeList.isEmpty()) {
                mThemeList!!.addAll(themeList)
            }
        }

        mThemeAdapter = GifThemeListAdapter(this, mThemeList!!)
        mThemeAdapter!!.setListClick(mListClick)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("选择GIF主题")

        rvThemeList = findViewById(R.id.rv_theme_list)
        rvThemeList!!.layoutManager = LinearLayoutManager(this)
        rvThemeList!!.adapter = mThemeAdapter
    }

    private val mListClick = object : OnListClickListener() {
        override fun onItemClick(position: Int) {
            val theme = mThemeList!![position]
            GifEditActivity.show(this@GifThemeListActivity, theme)
        }
    }
}