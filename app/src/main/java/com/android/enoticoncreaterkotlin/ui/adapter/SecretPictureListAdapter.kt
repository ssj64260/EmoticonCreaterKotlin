package com.android.enoticoncreaterkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.model.PictureInfo
import com.android.enoticoncreaterkotlin.util.ImageDataHelper
import com.android.enoticoncreaterkotlin.widget.imageloader.ImageLoaderFactory
import java.util.*

class SecretPictureListAdapter(context: Context) : RecyclerView.Adapter<SecretPictureListAdapter.BaseViewHolder>() {

    private var mListClick: IOnListClickListener? = null
    private val mContext: Context = context
    private var mDatas: ArrayList<PictureInfo>? = null
    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    init {
        mDatas = ArrayList()

        mDatas!!.clear()
        for (i in 0 until ImageDataHelper.SECRET_LIST.size) {
            val secret = PictureInfo()
            secret.resourceId = ImageDataHelper.SECRET_LIST[i]
            secret.title = ImageDataHelper.SECRET_TITLES[i]
            mDatas!!.add(secret)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_secret_picture_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mDatas!!.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindItem(holder, position)
    }

    private fun bindItem(holder: BaseViewHolder, position: Int) {
        val model = mDatas!!.get(position)
        val resourceId = model.resourceId
        val title = model.title

        ImageLoaderFactory.instance.loadImageFitCenter(mContext, holder.ivPicture, resourceId, 0, 0)
        holder.tvTitle.text = title

        holder.itemView.tag = holder.adapterPosition
        holder.itemView.setOnClickListener(mClick)
    }

    fun setListClick(listClick: IOnListClickListener) {
        mListClick = listClick
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPicture: AppCompatImageView = itemView.findViewById(R.id.iv_picture)
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tv_title)
    }

    private val mClick = View.OnClickListener { v ->
        if (mListClick != null) {
            val position = v.tag as Int
            val secret = mDatas!![position]
            mListClick!!.onItemClick(v, secret)
        }
    }
}