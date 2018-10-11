package com.android.enoticoncreaterkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.util.ImageDataHelper
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory
import com.android.enoticoncreaterkotlin.widget.imageloader.SquareImageView

class EmoticonListAdapter(context: Context, title: String) : RecyclerView.Adapter<EmoticonListAdapter.BaseViewHolder>() {

    private var mListClick: IOnListClickListener? = null
    private var mContext: Context = context
    private var mList: MutableList<PictureInfo>
    private var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(mContext)
        mList = ArrayList()
        val pictures: IntArray =
                when (title) {
                    "熊猫人" -> ImageDataHelper.XIONG_MAO_REN_LIST
                    "滑稽" -> ImageDataHelper.HUA_JI_LIST
                    else -> ImageDataHelper.MO_GU_TOU_LIST
                }

        mList.clear()
        for (picture in pictures) {
            val secret = PictureInfo()
            secret.resourceId = picture
            mList.add(secret)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_emoticon_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindItem(holder, position)
    }

    private fun bindItem(holder: BaseViewHolder, position: Int) {
        val model = mList[position]
        val resourceId = model.resourceId

        ImageLoaderFactory.instance.loadImageFitCenter(mContext, holder.ivPicture, resourceId,
                R.drawable.ic_photo, R.drawable.ic_photo)

        holder.itemView.tag = holder.adapterPosition
        holder.itemView.setOnClickListener(mClick)
    }

    fun setListClick(listClick: IOnListClickListener) {
        mListClick = listClick
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPicture: SquareImageView = itemView.findViewById(R.id.iv_picture)
    }

    private val mClick = View.OnClickListener { v ->
        if (mListClick != null) {
            val position = v.tag as Int
            val secret = mList[position]
            mListClick!!.onItemClick(v, secret)
        }
    }
}