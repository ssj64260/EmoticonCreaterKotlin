package com.android.enoticoncreaterkotlin.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseActivity
import com.android.enoticoncreaterkotlin.config.Constants
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.ui.adapter.SecretListAdapter
import com.android.enoticoncreaterkotlin.util.FileUtils
import com.android.enoticoncreaterkotlin.util.SDCardUtils
import com.android.enoticoncreaterkotlin.util.SecretHelper
import com.android.enoticoncreaterkotlin.util.ThreadPoolUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.*

class TellTheSecretActivity : BaseActivity() {

    companion object {
        private const val KEY_NEW_SECRET = "key_new_secret"

        fun showOnNewIntent(activity: Activity, secret: PictureInfo) {
            val intent = Intent()
            intent.setClass(activity, TellTheSecretActivity().javaClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra(KEY_NEW_SECRET, secret)
            activity.startActivity(intent)
        }

        fun show(activity: Activity) {
            val intent = Intent()
            intent.setClass(activity, TellTheSecretActivity().javaClass)
            activity.startActivity(intent)
        }
    }

    private var rvSecretList: RecyclerView? = null
    private var btnAdd: FloatingActionButton? = null

    private var mSecretList: ArrayList<PictureInfo>? = null
    private var mSecretAdapter: SecretListAdapter? = null

    private var mLayoutManager: LinearLayoutManager? = null
    private var mItemTouchHelper: ItemTouchHelper? = null

    private var mSavePath: String? = null

    override fun getContentView(): Int {
        return R.layout.activity_tell_the_secret
    }

    override fun initData() {
        super.initData()
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_SECRET
        FileUtils.createdirectory(mSavePath!!)

        mSecretList = ArrayList()
        mSecretAdapter = SecretListAdapter(this, mSecretList!!)

        mLayoutManager = LinearLayoutManager(this)
        mItemTouchHelper = ItemTouchHelper(mCallback)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setToolbarBackEnable()
        setToolbarTitle("告诉你个秘密")

        rvSecretList = findViewById(R.id.rv_secret_list)
        btnAdd = findViewById(R.id.btn_add)

        rvSecretList!!.layoutManager = mLayoutManager
        rvSecretList!!.adapter = mSecretAdapter

        mItemTouchHelper!!.attachToRecyclerView(rvSecretList)

        btnAdd!!.setOnClickListener(mClick)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val secret = intent?.getParcelableExtra<PictureInfo>(KEY_NEW_SECRET)
        if (secret != null) {
            val index = mSecretList!!.size
            mSecretList!!.add(secret)
            mSecretAdapter!!.notifyItemInserted(index)
            rvSecretList!!.scrollToPosition(index)
        }
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

    private fun doAdd() {
        if (mSecretList!!.size >= 10) {
            showSnackbar("最多只能添加10个秘密")
        } else {
            SecretListActivity.show(this)
        }
    }

    private fun doCreate() {
        if (mSecretList!!.size <= 0) {
            showSnackbar("你还没添加秘密")
        } else {
            showProgress("图片处理中...")
            ThreadPoolUtil.instance.cachedExecute(Runnable {
                val typeface = Typeface.createFromAsset(assets, "fonts/bold.ttf")
                val imageFile = SecretHelper.createSecret(resources, mSecretList!!, mSavePath!!, typeface)

                runOnUiThread {
                    if (imageFile.exists()) {
                        val filePath = imageFile.absolutePath
                        refreshAlbum(imageFile)

                        ShowPictureActivity.show(this@TellTheSecretActivity, filePath)
                    } else {
                        showSnackbar("生成失败，图片不存在")
                    }
                    hideProgress()
                }
            })
        }
    }

    private fun refreshAlbum(file: File?) {
        if (file != null && file.exists()) {
            val uri = Uri.fromFile(file)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showFloatingButton() {
        btnAdd!!.visibility = View.VISIBLE
        ViewCompat.animate(btnAdd!!)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setInterpolator(FastOutSlowInInterpolator()).withLayer().setListener(null)
                .start()
    }

    private val mClick = View.OnClickListener { v ->
        when (v.id) {
            R.id.btn_add -> doAdd()
            R.id.btn_do_create -> doCreate()
        }
    }

    private val mCallback = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.LEFT
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            Collections.swap(mSecretList, fromPosition, toPosition)
            mSecretAdapter!!.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val count = mSecretList!!.size
            val position = viewHolder.adapterPosition
            mSecretList!!.removeAt(position)
            mSecretAdapter!!.notifyItemRemoved(position)
            mSecretAdapter!!.notifyItemRangeChanged(position, count - position)
            showFloatingButton()
        }
    }
}