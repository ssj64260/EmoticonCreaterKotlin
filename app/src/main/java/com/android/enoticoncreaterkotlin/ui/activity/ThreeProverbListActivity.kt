package com.android.enoticoncreaterkotlin.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.db.LiteOrmHelper
import com.android.enoticoncreaterkotlin.model.ThreeProverbInfo
import com.android.enoticoncreaterkotlin.ui.adapter.IOnListClickListener.Companion.ITEM_TAG0
import com.android.enoticoncreaterkotlin.ui.adapter.OnListClickListener
import com.android.enoticoncreaterkotlin.ui.adapter.ThreeProverbListAdapter
import com.android.enoticoncreaterkotlin.widget.dialog.AlertDialogFragment
import java.util.*

class ThreeProverbListActivity : BaseActivity() {

    companion object {
        fun show(activity: Activity, requestCode: Int) {
            val intent = Intent()
            intent.setClass(activity, ThreeProverbListActivity().javaClass)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var rvProverbList: RecyclerView? = null

    private var mAlertDialog: AlertDialogFragment? = null

    private var mProverbList: ArrayList<ThreeProverbInfo>? = null
    private var mProverbAdapter: ThreeProverbListAdapter? = null

    private var mDBHelper: LiteOrmHelper? = null

    override fun getContentView(): Int {
        return R.layout.activity_three_proverb_list
    }

    override fun initData() {
        super.initData()
        mDBHelper = LiteOrmHelper(this)

        mProverbList = ArrayList()
        mProverbAdapter = ThreeProverbListAdapter(this, mProverbList!!)
        mProverbAdapter!!.setListClick(mListClick)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("怼人语录")

        rvProverbList = findViewById(R.id.rv_proverb_list)
        rvProverbList!!.layoutManager = GridLayoutManager(this, 2)
        rvProverbList!!.adapter = mProverbAdapter

        getProverbList()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDBHelper?.closeDB()
    }

    private fun showDeleteDialog(position: Int) {
        if (mAlertDialog == null) {
            mAlertDialog = AlertDialogFragment()
        }
        mAlertDialog!!.setMessage("是否删除这条语录？")
        mAlertDialog!!.setCancelButton("取消")
        mAlertDialog!!.setConfirmButton("删除", DialogInterface.OnClickListener { dialog, which ->
            val proverb = mProverbList!![position]
            mDBHelper!!.delete(proverb)
            val count = mProverbList!!.size
            if (position < count) {
                mProverbList!!.removeAt(position)
                mProverbAdapter!!.notifyItemRemoved(position)
                mProverbAdapter!!.notifyItemRangeChanged(position, count - position)
            }
            showSnackbar("删除成功")
            mAlertDialog!!.dismiss()
        })

        mAlertDialog!!.show(supportFragmentManager, "DeleteDialog")
    }

    private fun getProverbList() {
        val proverbList = mDBHelper!!.queryAllOrderDescBy(ThreeProverbInfo().javaClass, "useTimes")
        mProverbList!!.clear()
        mProverbList!!.addAll(proverbList)
        mProverbAdapter!!.notifyDataSetChanged()

        if (mProverbList!!.size == 0) {
            showSnackbar("一句话都没有")
        }
    }

    private val mListClick = object : OnListClickListener() {
        override fun onItemClick(position: Int) {
            val proverb = mProverbList!!.get(position)
            val intent = Intent()
            intent.putExtra(Constants.KEY_RETURN_DATA, proverb)
            setResult(RESULT_OK, intent)
            finish()
        }

        override fun onTagClick(tag: Int, position: Int) {
            if (ITEM_TAG0 == tag) {
                showDeleteDialog(position)
            }
        }
    }
}