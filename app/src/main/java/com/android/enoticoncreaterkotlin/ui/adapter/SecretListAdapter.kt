package com.android.enoticoncreaterkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.model.PictureInfo
import java.util.*

class SecretListAdapter(context: Context, datas: ArrayList<PictureInfo>) : RecyclerView.Adapter<SecretListAdapter.BaseViewHolder>() {

    private val mDatas: ArrayList<PictureInfo> = datas
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_secret_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindItem(holder, position)
    }

    private fun bindItem(holder: BaseViewHolder, position: Int) {
        val model = mDatas[position]
        val resourceId = model.resourceId
        val title = model.title

        holder.ivPicture.setImageResource(resourceId)
        holder.tvTitle.text = title
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPicture: ImageView = itemView.findViewById(R.id.iv_picture)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
}