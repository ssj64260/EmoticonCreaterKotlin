package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.ui.adapter.OnListClickListener
import com.android.enoticoncreaterkotlin.ui.adapter.SecretPictureListAdapter

class SecretListActivity : BaseActivity() {

    companion object {
        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, SecretListActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var rvSecretList: RecyclerView? = null

    private var mSecretAdapter: SecretPictureListAdapter? = null

    override fun getContentView(): Int {
        return R.layout.activity_secret_list
    }

    override fun initData() {
        super.initData()
        mSecretAdapter = SecretPictureListAdapter(this)
        mSecretAdapter!!.setListClick(mListClick)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("秘密表情包")
        setToolbarSubTitle("选择一个秘密表情")

        rvSecretList = findViewById(R.id.rv_secret_list)
        rvSecretList!!.layoutManager = LinearLayoutManager(this)
        rvSecretList!!.adapter = mSecretAdapter
    }

    private val mListClick = object : OnListClickListener() {
        override fun onItemClick(view: View, obj: Any) {
            if (obj is PictureInfo) {
                val picturePair = Pair.create(view, getString(R.string.transition_name_secret))

                val options = ActivityOptions.makeSceneTransitionAnimation(this@SecretListActivity, picturePair)

                SecretEditActivity.show(this@SecretListActivity, options, obj)
            }
        }
    }
}