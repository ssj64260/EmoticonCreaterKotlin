package com.android.enoticoncreaterkotlin.ui.fragment

import android.app.ActivityOptions
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.app.BaseFragment
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.ui.activity.OneEmoticonEditActivity
import com.android.enoticoncreaterkotlin.ui.adapter.EmoticonListAdapter
import com.android.enoticoncreaterkotlin.ui.adapter.OnListClickListener

class EmoticonFragment : BaseFragment() {

    companion object {
        private const val ARGUMENT = "argument"

        fun newInstance(title: String): EmoticonFragment {
            val bundle = Bundle()
            bundle.putString(ARGUMENT, title)
            val fragment = EmoticonFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var rvEmoticonList: RecyclerView? = null

    private var mEmoticonAdapter: EmoticonListAdapter? = null
    private var mTitle: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_emoticon
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            mTitle = bundle.getString(ARGUMENT)
        }

        mEmoticonAdapter = EmoticonListAdapter(mActivity!!, mTitle!!)
        mEmoticonAdapter!!.setListClick(mListClick)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        rvEmoticonList = mFragmentView!!.findViewById(R.id.rv_emoticon_list)

        rvEmoticonList!!.layoutManager = GridLayoutManager(mActivity, 3)
        rvEmoticonList!!.adapter = mEmoticonAdapter
    }

    private val mListClick = object : OnListClickListener() {
        override fun onItemClick(view: View, obj: Any) {
            if (obj is PictureInfo) {
                val image: View = view.findViewById(R.id.iv_picture)
                val picturePair = Pair.create(image, getString(R.string.transition_name_one_emoticon))
                val options = ActivityOptions.makeSceneTransitionAnimation(mActivity, picturePair)

                OneEmoticonEditActivity.show(mActivity!!, options, obj)
            }
        }
    }
}