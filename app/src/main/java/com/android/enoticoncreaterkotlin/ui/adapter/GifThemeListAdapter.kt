package com.android.enoticoncreaterkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.model.GifTheme

class GifThemeListAdapter(context: Context, datas: ArrayList<GifTheme>) : RecyclerView.Adapter<GifThemeListAdapter.BaseViewHolder>() {

    private var mDatas: List<GifTheme> = datas
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mListClick: IOnListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_gif_theme_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindItem(holder, position)
    }

    private fun bindItem(holder: BaseViewHolder, position: Int) {
        val data = mDatas[position]
        val name = data.name

        holder.btnTheme.text = name
        holder.btnTheme.tag = position
        holder.btnTheme.setOnClickListener(mClick)
    }

    fun setListClick(listClick: IOnListClickListener) {
        this.mListClick = listClick
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnTheme: Button = itemView.findViewById(R.id.btn_theme)
    }

    private val mClick = View.OnClickListener { v ->
        if (mListClick != null) {
            val position = v.tag as Int
            mListClick!!.onItemClick(position)
        }
    }
}