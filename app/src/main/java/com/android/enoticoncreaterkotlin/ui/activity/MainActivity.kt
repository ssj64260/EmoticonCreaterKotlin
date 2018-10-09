package com.android.enoticoncreaterkotlin.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.model.FunctionInfo
import com.android.enoticoncreaterkotlin.ui.adapter.FunctionListAdapter
import com.android.enoticoncreaterkotlin.ui.adapter.OnListClickListener
import com.android.enoticoncreaterkotlin.util.FastClick
import com.android.enoticoncreaterkotlin.util.FileUtils
import com.android.enoticoncreaterkotlin.util.PermissionsHelper
import com.android.enoticoncreaterkotlin.util.SDCardUtils

class MainActivity : BaseActivity() {

    private lateinit var rvFunctionList: RecyclerView
    private lateinit var mFunctionList: ArrayList<FunctionInfo>
    private lateinit var mFunctionAdapter: FunctionListAdapter

    private lateinit var mPermissionsHelper: PermissionsHelper

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        super.initData()
        mFunctionList = FunctionInfo.createList()
        mFunctionAdapter = FunctionListAdapter(this, mFunctionList)

        mPermissionsHelper = PermissionsHelper.Builder()
                .writeExternalStorage()
                .readExternalStorage()
                .setPermissionsResult(mPermissionsResult)
                .bulid()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarTitle(R.string.app_name)

        rvFunctionList = findViewById(R.id.rv_function_list)
        rvFunctionList.layoutManager = LinearLayoutManager(this)
        rvFunctionList.adapter = mFunctionAdapter

        mPermissionsHelper.requestPermissions(this)
    }

    private fun setData() {
        mFunctionAdapter.setListClick(mListClick)
        val basePath = SDCardUtils.getSDCardDir() + Constants.PATH_BASE
        if (!FileUtils.createdirectory(basePath)) {
            showSnackbar("创建存储目录失败")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionsHelper.requestPermissionsResult(this, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPermissionsHelper.activityResult(this, requestCode)
    }

    override fun onBackPressed() {
        if (!FastClick.isExitClick()) {
            showSnackbar("再次点击退出程序")
        } else {
            super.onBackPressed()
        }
    }

    private val mListClick = object : OnListClickListener() {
        override fun onItemClick(position: Int) {
            val function = mFunctionList[position]
            val name = function.getName()

            when (name) {
                FunctionInfo.NAME_TRIPLE_SEND -> showSnackbar(FunctionInfo.NAME_TRIPLE_SEND)
                FunctionInfo.NAME_SECRET -> showSnackbar(FunctionInfo.NAME_SECRET)
                FunctionInfo.NAME_ONE_EMOTICON -> showSnackbar(FunctionInfo.NAME_ONE_EMOTICON)
                FunctionInfo.NAME_GIF -> showSnackbar(FunctionInfo.NAME_GIF)
                FunctionInfo.NAME_MATURE -> showSnackbar(FunctionInfo.NAME_MATURE)
            }
        }
    }

    private val mPermissionsResult = object : PermissionsHelper.OnPermissionsResult {
        override fun allPermissionGranted() {
            setData()
        }

        override fun cancelToSettings() {
            finish()
        }
    }
}
